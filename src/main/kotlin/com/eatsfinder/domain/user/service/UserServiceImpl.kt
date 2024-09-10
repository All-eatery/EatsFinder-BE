package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.email.service.EmailUtils
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.dto.user.*
import com.eatsfinder.domain.user.dto.user.active.MyActiveResponse
import com.eatsfinder.domain.user.model.DeleteUserData
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.DeleteUserReason
import com.eatsfinder.domain.user.repository.DeleteUserDataRepository
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.aws.AwsS3Service
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.email.ExpiredCodeException
import com.eatsfinder.global.exception.email.NotCheckCompleteException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import com.eatsfinder.global.exception.profile.*
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository,
    private val passwordEncoder: PasswordEncoder,
    private val postRepository: PostRepository,
    private val userLogRepository: UserLogRepository,
    private val deleteUserDataRepository: DeleteUserDataRepository,
    private val emailUtils: EmailUtils,
    private val awsService: AwsS3Service
) : UserService {

    @Transactional(readOnly = true)
    override fun getMyProfile(myProfileId: Long): MyProfileResponse {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${myProfileId})은 존재하지 않습니다."
        )
        val postCount = postRepository.findByUserId(profile)?.size ?: 0
        return MyProfileResponse.from(profile, postCount)
    }

    @Transactional(readOnly = true)
    override fun profileViewedByOthers(otherProfileId: Long): ProfileViewedByOthersResponse {
        val profile = userRepository.findByIdAndDeletedAt(otherProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${otherProfileId})은 존재하지 않습니다."
        )
        val postCount = postRepository.findByUserId(profile)?.size ?: 0

        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal

        if (userPrincipal != null && profile.id == userPrincipal.id) {
            throw MyProfileException("본인 프로필이므로 조회할 수 없습니다.")
        }
        return ProfileViewedByOthersResponse.from(profile, postCount)
    }

    @Transactional
    override fun updateProfile(req: UpdateProfileRequest, myProfileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        req.profileImage?.let {
            profile.profileImage?.let { image ->
                awsService.deleteImage(image)
            }

            val uploadUrl = awsService.uploadImage(req.profileImage)
            profile.profileImage = uploadUrl
        }

        profile.nickname = req.nickname ?: profile.nickname
        profile.phoneNumber = req.phoneNumber ?: profile.phoneNumber
        userRepository.save(profile)
    }

    @Transactional
    override fun defaultProfileImage(myProfileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        if (profile.profileImage == null){
            throw AlreadyDefaultProfileImageException("이미 기본 프로필인 상태입니다.")
        }

        profile.profileImage?.let { image ->
            awsService.deleteImage(image)
            profile.profileImage = null
            userRepository.save(profile)
        }

    }

    @Transactional
    override fun changePassword(req: ChangePasswordRequest, myProfileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        when {
            profile.provider != SocialType.LOCAL -> throw ImmutableUserException("비밀번호를 변경할 수 없는 소셜 유저입니다.")
            passwordEncoder.matches(
                req.newPassword,
                profile.password
            ) -> throw WrongPasswordException("기존에 있던 비밀번호와 같습니다.")

            req.newPassword != req.passwordConfirm -> throw WrongPasswordException("새로운 비밀번호와 확인 비밀번호가 맞지 않습니다. 다시 입력해주세요.")
            else -> {
                profile.password = passwordEncoder.encode(req.newPassword)
                userRepository.save(profile)
            }
        }
    }

    override fun deleteProfile(
        myProfileId: Long,
        email: String,
        code: String,
        unavailability: Boolean,
        infrequent: Boolean,
        privacy: Boolean,
        inconvenience: Boolean,
        switching: Boolean,
        others: Boolean,
        reason: String?
    ) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )
        if (others && reason == null){
            throw EnterAddInfoException("기타 사유를 입력해주세요")
        }

        val checkCode = emailRepository.findByCode(code)
            ?: throw OneTimeMoreWriteException("다시 한번 입력해주세요")

        when {
            checkCode.expiredAt.isBefore(LocalDateTime.now()) ->
                throw ExpiredCodeException("인증번호가 만료되었습니다.")
            !checkCode.complete ->
                throw NotCheckCompleteException("인증확인이 되지 않았습니다.")
            checkCode.code != code || profile.email != checkCode.email || profile.email != email ->
                throw OneTimeMoreWriteException("다시 한번 입력해주세요")
        }

        userRepository.delete(profile)
        emailUtils.guideEmail(email)
        deleteUserDataRepository.save(
            DeleteUserData(
                userId = profile,
                userEmail = profile.email,
                unavailability = unavailability,
                infrequent = infrequent,
                privacy = privacy,
                inconvenience = inconvenience,
                switching = switching,
                others = others,
                reason = reason
            )
        )
    }

    override fun getMyFeed(myProfileId: Long): List<MyFeedResponse> {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )
        return postRepository.findByUserId(profile)!!.map { MyFeedResponse.from(it) }
    }

    override fun getMyActive(myProfileId: Long): List<MyActiveResponse> {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )
        val logs = userLogRepository.findByUserId(profile)?.distinct() ?: emptyList()
        return if (logs.isNotEmpty()) {
            listOf(MyActiveResponse.from(logs))
        } else {
            emptyList()
        }
    }
}
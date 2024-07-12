package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.email.service.EmailUtils
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.dto.profile.ChangePasswordRequest
import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.aws.AwsS3Service
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.email.ExpiredCodeException
import com.eatsfinder.global.exception.email.NotCheckCompleteException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import com.eatsfinder.global.exception.profile.ImmutableUserException
import com.eatsfinder.global.exception.profile.MyProfileException
import com.eatsfinder.global.exception.profile.NotMismatchProfileImageException
import com.eatsfinder.global.exception.profile.WrongPasswordException
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class ProfileServiceImpl(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository,
    private val passwordEncoder: PasswordEncoder,
    private val postRepository: PostRepository,
    private val emailUtils: EmailUtils,
    private val awsService: AwsS3Service
) : ProfileService {

    override fun getMyProfile(myProfileId: Long): MyProfileResponse {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${myProfileId})은 존재하지 않습니다."
        )
        val postCount = postRepository.findByUserId(profile)?.size ?: 0
        return MyProfileResponse.from(profile, postCount)
    }

    override fun profileViewedByOthers(profileId: Long): ProfileViewedByOthersResponse {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${profileId})은 존재하지 않습니다."
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

        if (profile.provider != SocialType.LOCAL) {
            throw ImmutableUserException("프로필 수정할 수 없는 소셜 유저입니다.")
        }

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

    override fun deleteProfileImage(profileImage: MultipartFile?, myProfileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        if (profile.provider != SocialType.LOCAL) {
            throw ImmutableUserException("프로필 이미지를 삭제할 수 없는 소셜 유저입니다.")
        }

        profileImage?.let { image ->
            if (profile.profileImage != null && !awsService.compareImages(profileImage, image.toString())) {
                throw NotMismatchProfileImageException("이 프로필 이미지는 기존에 업로된 이미지와 일치하지 않습니다.")
            }
        }

        profile.profileImage?.let { image ->
            awsService.deleteImage(image)
        }
        profile.profileImage = null
        userRepository.save(profile)
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

    override fun deleteProfile(myProfileId: Long, email: String, code: String) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        val checkCode = emailRepository.findByCode(code)
        when {
            checkCode == null || !(checkCode.code == code && profile.email == checkCode.email && profile.email == email) -> throw OneTimeMoreWriteException("다시 한번 입력해주세요")
            checkCode.expiredAt.isBefore(LocalDateTime.now()) -> throw ExpiredCodeException("인증번호가 만료되었습니다.")
            !checkCode.complete-> throw NotCheckCompleteException(
                "인증확인이 되지 않았습니다."
            )
            else -> {
                userRepository.delete(profile)
                emailUtils.guideEmail(email)
            }
        }
    }
}
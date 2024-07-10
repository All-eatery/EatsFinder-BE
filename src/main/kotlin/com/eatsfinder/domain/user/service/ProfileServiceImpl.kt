package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.user.dto.profile.ChangePasswordRequest
import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import com.eatsfinder.global.exception.profile.ImmutableUserException
import com.eatsfinder.global.exception.profile.MyProfileException
import com.eatsfinder.global.exception.profile.NotMyProfileException
import com.eatsfinder.global.exception.profile.WrongPasswordException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository,
    private val passwordEncoder: PasswordEncoder,
) : ProfileService {

    override fun getMyProfile(myProfileId: Long): MyProfileResponse {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${myProfileId})은 존재하지 않습니다."
        )
        if (profile.id != myProfileId){
            throw NotMyProfileException("본인 프로필이 아닙니다.")
        }

        return MyProfileResponse.from(profile)
    }

    override fun profileViewedByOthers(profileId: Long, myProfileId: Long): ProfileViewedByOthersResponse {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${profileId})은 존재하지 않습니다."
        )

        if (profile.id == myProfileId){
            throw MyProfileException("본인 프로필이므로 조회할 수 없습니다.")
        }

        return ProfileViewedByOthersResponse.from(profile)
    }

    // 이미지 수정(원래 있던거 삭제 -> 추가), 이미지 없을 경우 -> 추가
    @Transactional
    override fun updateProfile(req: UpdateProfileRequest, myProfileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(myProfileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${myProfileId})은 존재하지 않습니다."
        )

        if (profile.provider != SocialType.LOCAL) {
            throw ImmutableUserException("프로필 수정할 수 없는 소셜 유저입니다.")
        }

        if (profile.id != myProfileId){
            throw NotMyProfileException("본인 프로필이 아닙니다.")
        }

        profile.nickname = req.nickname ?: profile.nickname
        profile.phoneNumber = req.phoneNumber ?: profile.phoneNumber
        profile.profileImage = req.profileImage ?: profile.profileImage
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
            passwordEncoder.matches(req.newPassword, profile.password) -> throw WrongPasswordException("기존에 있던 비밀번호와 같습니다.")
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

        if (profile.id != myProfileId){
            throw NotMyProfileException("본인 프로필이 아닙니다.")
        }

        val checkCode = emailRepository.findByCodeAndComplete("", true)
        if (checkCode == null || !(checkCode.code == code && profile.email == checkCode.email && profile.email == email)){
            throw OneTimeMoreWriteException("다시 한번 입력해주세요")
        }
        userRepository.delete(profile)
    }

}
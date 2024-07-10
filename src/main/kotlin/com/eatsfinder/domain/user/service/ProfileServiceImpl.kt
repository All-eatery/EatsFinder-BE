package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import com.eatsfinder.global.exception.profile.ImmutableUserException
import com.eatsfinder.global.exception.profile.NotMyProfileException
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository
) : ProfileService {

    override fun getMyProfile(profileId: Long): MyProfileResponse {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${profileId})은 존재하지 않습니다."
        )
//        if (profile.id != profileId){
//            throw NotMyProfileException("본인 프로필이 아닙니다.")
//        }

        return MyProfileResponse.from(profile)
    }

    // 다른 유저가 보는 것이므로 마이 프로필인 사람은 안나오게 해야함.
    override fun profileViewedByOthers(profileId: Long): ProfileViewedByOthersResponse {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id: ${profileId})은 존재하지 않습니다."
        )

//        if (profile.id == profileId){
//            throw NotMyProfileException("본인 프로필이므로 조회할 수 없습니다.")
//        }

        return ProfileViewedByOthersResponse.from(profile)
    }

    // 이미지 수정(원래 있던거 삭제 -> 추가), 이미지 없을 경우 -> 추가
    override fun updateProfile(req: UpdateProfileRequest, profileId: Long) {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${profileId})은 존재하지 않습니다."
        )

//        if (profile.provider != SocialType.LOCAL) {
//            throw ImmutableUserException("프로필 수정할 수 없는 소셜 유저입니다.")
//        }

        if (profile.id != profileId){
            throw NotMyProfileException("본인 프로필이 아닙니다.")
        }

        profile.nickname = req.nickname ?: profile.nickname
        profile.phoneNumber = req.phoneNumber ?: profile.phoneNumber
        profile.profileImage = req.profileImage ?: profile.profileImage
        userRepository.save(profile)
    }

    override fun deleteProfile(profileId: Long, email: String, code: String) {
        val profile = userRepository.findByIdAndDeletedAt(profileId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필은(id: ${profileId})은 존재하지 않습니다."
        )

//        if (profile.id != profileId){
//            throw NotMyProfileException("본인 프로필이 아닙니다.")
//        }

        val checkCode = emailRepository.findByCodeAndComplete("", true)
        if (checkCode == null || !(checkCode.code == code && profile.email == checkCode.email && profile.email == email)){
            throw OneTimeMoreWriteException("다시 한번 입력해주세요")
        }
        userRepository.delete(profile)
    }

}
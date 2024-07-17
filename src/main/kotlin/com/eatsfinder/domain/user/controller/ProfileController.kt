package com.eatsfinder.domain.user.controller

import com.eatsfinder.domain.user.dto.profile.ChangePasswordRequest
import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest
import com.eatsfinder.domain.user.service.ProfileService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(
    private val profileService: ProfileService
) {
    @Operation(summary = "본인 프로필 조회하기")
    @GetMapping("/my-profile")
    fun getMyProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<MyProfileResponse> {
        val myProfileId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getMyProfile(myProfileId))
    }

    @Operation(summary = "다른 유저 프로필 조회하기")
    @GetMapping("/profile/{profileId}")
    fun profileViewedByOthers(@PathVariable profileId: Long): ResponseEntity<ProfileViewedByOthersResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.profileViewedByOthers(profileId))
    }

    @Operation(summary = "본인 프로필 수정하기")
    @PatchMapping("/my-profile", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProfile(@ModelAttribute req: UpdateProfileRequest, @AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.updateProfile(req, myProfileId)
        return BaseResponse(message = "프로필이 수정되었습니다.")
    }

    @Operation(summary = "프로필 이미지 삭제하기 : 기본 프로필로 전환")
    @PutMapping("/my-profile/default-images")
    fun defaultProfileImage(@AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.defaultProfileImage(myProfileId)
        return BaseResponse(message = "프로필이 삭제되었습니다.")
    }

    @Operation(summary = "비밀번호 재설정")
    @PutMapping("/my-profile/new-password")
    fun changePassword(@RequestBody @Valid req: ChangePasswordRequest, @AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.changePassword(req, myProfileId)
        return BaseResponse(message = "비밀번호가 수정되었습니다.")
    }

    @Operation(summary = "탈퇴하기")
    @DeleteMapping("/my-profile")
    fun deleteProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestParam email: String, @RequestParam code: String): ResponseEntity<Unit> {
        val myProfileId = userPrincipal.id
        profileService.deleteProfile(myProfileId, email, code)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
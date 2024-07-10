package com.eatsfinder.domain.user.controller

import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest
import com.eatsfinder.domain.user.service.ProfileService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
    fun profileViewedByOthers(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable profileId: Long): ResponseEntity<ProfileViewedByOthersResponse> {
        val myProfileId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(profileService.profileViewedByOthers(profileId, myProfileId))
    }

    @Operation(summary = "본인 프로필 수정하기")
    @PatchMapping("/my-profile")
    fun updateProfile(@RequestBody req: UpdateProfileRequest, @AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.updateProfile(req, myProfileId)
        return BaseResponse(message = "프로필이 수정되었습니다.")
    }

    @Operation(summary = "탈퇴하기")
    @DeleteMapping("/my-profile")
    fun deleteProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestParam email: String, @RequestParam code: String): ResponseEntity<Unit> {
        val myProfileId = userPrincipal.id
        profileService.deleteProfile(myProfileId, email, code)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
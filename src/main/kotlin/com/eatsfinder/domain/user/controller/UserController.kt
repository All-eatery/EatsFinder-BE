package com.eatsfinder.domain.user.controller

import com.eatsfinder.domain.user.dto.user.*
import com.eatsfinder.domain.user.dto.user.active.MyActiveResponse
import com.eatsfinder.domain.user.service.UserService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val profileService: UserService
) {
    @Operation(summary = "본인 프로필 조회하기")
    @GetMapping
    fun getMyProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<MyProfileResponse> {
        val myProfileId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getMyProfile(myProfileId))
    }

    @Operation(summary = "다른 유저 프로필 조회하기")
    @GetMapping("/{otherProfileId}")
    fun profileViewedByOthers(@PathVariable otherProfileId: Long): ResponseEntity<ProfileViewedByOthersResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.profileViewedByOthers(otherProfileId))
    }

    @Operation(summary = "본인 프로필 수정하기")
    @PatchMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProfile(@ModelAttribute req: UpdateProfileRequest, @AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.updateProfile(req, myProfileId)
        return BaseResponse(message = "프로필이 수정되었습니다.")
    }

    @Operation(summary = "프로필 이미지 삭제하기 : 기본 프로필로 전환")
    @PutMapping("/default-image")
    fun defaultProfileImage(@AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.defaultProfileImage(myProfileId)
        return BaseResponse(message = "프로필이 삭제되었습니다.")
    }

    @Operation(summary = "비밀번호 재설정")
    @PutMapping("/new-password")
    fun changePassword(@RequestBody @Valid req: ChangePasswordRequest, @AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.changePassword(req, myProfileId)
        return BaseResponse(message = "비밀번호가 수정되었습니다.")
    }

    @Operation(summary = "탈퇴하기")
    @DeleteMapping
    fun deleteProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal,
                      @RequestParam email : String,
                      @RequestParam (required = false) unavailability : Boolean,
                      @RequestParam (required = false) infrequent : Boolean,
                      @RequestParam (required = false) privacy : Boolean,
                      @RequestParam (required = false) inconvenience : Boolean,
                      @RequestParam (required = false) switching : Boolean,
                      @RequestParam (required = false) others : Boolean,
                      @RequestParam reason : String?,
    ): ResponseEntity<Unit> {
        val myProfileId = userPrincipal.id
        profileService.deleteProfile(myProfileId, email, unavailability, infrequent, privacy, inconvenience, switching, others, reason)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "탈퇴 철회하기")
    @PutMapping
    fun cancelWithdrawal(@AuthenticationPrincipal userPrincipal: UserPrincipal): BaseResponse<Unit> {
        val myProfileId = userPrincipal.id
        profileService.cancelWithdrawal(myProfileId)
        return BaseResponse(message = "탈퇴 철회가 완료되었습니다.")
    }

    @Operation(summary = "내 피드 조회하기")
    @GetMapping("/feeds")
    fun getMyFeed(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PageableDefault(size = 10, sort = ["updatedAt"]) pageable: Pageable
    ): ResponseEntity<MyFeedsResponse> {
        val myProfileId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getMyFeed(myProfileId, pageable))
    }

    @Operation(summary = "다른 사람 피드 조회하기")
    @GetMapping("/feeds/{otherProfileId}")
    fun getMyFeed(
        @PathVariable otherProfileId: Long,
        @PageableDefault(size = 10, sort = ["updatedAt"]) pageable: Pageable
    ): ResponseEntity<OtherPeopleFeedsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getOtherPeopleFeed(otherProfileId, pageable))
    }

    @Operation(summary = "내 활동 조회하기")
    @GetMapping("/actives")
    fun getMyActive(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PageableDefault(size = 10, sort = ["createdAt"]) pageable: Pageable
        ): ResponseEntity<List<MyActiveResponse>> {

        val myProfileId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getMyActive(myProfileId, pageable))
    }
}
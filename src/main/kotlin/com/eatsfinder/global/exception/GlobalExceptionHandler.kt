package com.eatsfinder.global.exception

import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.exception.email.*
import com.eatsfinder.global.exception.like.DefaultZeroException
import com.eatsfinder.global.exception.profile.*
import com.eatsfinder.global.exception.status.StatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ModelNotFoundException::class)
    protected fun modelNotFoundException(ex: ModelNotFoundException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotGenerateCodeException::class)
    protected fun notGenerateCodeException(ex: NotGenerateCodeException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ExpiredCodeException::class)
    protected fun expiredCodeException(ex: ExpiredCodeException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoMatchEmailException::class)
    protected fun noMatchEmailException(ex: NoMatchEmailException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(OneTimeMoreWriteException::class)
    protected fun expiredCodeException(ex: OneTimeMoreWriteException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ImmutableUserException::class)
    protected fun immutableUserException(ex: ImmutableUserException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(MyProfileException::class)
    protected fun myProfileException(ex: MyProfileException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(WrongPasswordException::class)
    protected fun wrongPasswordException(ex: WrongPasswordException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotUploadImageException::class)
    protected fun notUploadImageException(ex: NotUploadImageException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotCheckCompleteException::class)
    protected fun notCheckCompleteException(ex: NotCheckCompleteException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AlreadyDefaultProfileImageException::class)
    protected fun alreadyDefaultProfileImageException(ex: AlreadyDefaultProfileImageException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DefaultZeroException::class)
    protected fun defaultZeroException(ex: DefaultZeroException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AlreadyExistException::class)
    protected fun alreadyExistException(ex: AlreadyExistException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(WithdrawalReasonException::class)
    protected fun withdrawalReasonException(ex: WithdrawalReasonException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoChangeNicknameAtException::class)
    protected fun noChangeNicknameAtException(ex: NoChangeNicknameAtException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoCancelWithdrawalException::class)
    protected fun noCancelWithdrawalException(ex: NoCancelWithdrawalException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }
}
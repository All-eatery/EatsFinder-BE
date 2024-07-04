package com.eatsfinder.global.exception

import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.exception.dto.ErrorResponse
import com.eatsfinder.global.exception.email.ExpiredCodeException
import com.eatsfinder.global.exception.email.NotGenerateCodeException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import com.eatsfinder.global.exception.status.StatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(message = e.message))
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

    @ExceptionHandler(OneTimeMoreWriteException::class)
    protected fun expiredCodeException(ex: OneTimeMoreWriteException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(StatusCode.ERROR.name, errors, StatusCode.ERROR.message), HttpStatus.BAD_REQUEST)
    }
}
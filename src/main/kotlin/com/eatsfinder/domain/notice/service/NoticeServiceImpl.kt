package com.eatsfinder.domain.notice.service

import com.eatsfinder.domain.notice.dto.NoticeRequest
import com.eatsfinder.domain.notice.dto.NoticeResponse
import com.eatsfinder.domain.notice.model.Notice
import com.eatsfinder.domain.notice.repository.NoticeRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.profile.ImmutableUserOrUnauthorizedUserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeServiceImpl(
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository
) : NoticeService {
    override fun getNoticeList(userId: Long): List<NoticeResponse> {
        val notice = noticeRepository.findAllByDeletedAt(null)
        return notice!!.map { NoticeResponse.from(it) }
    }

    override fun getNotice(noticeId: Long, userId: Long): NoticeResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val notice = noticeRepository.findByIdAndDeletedAt(noticeId, null) ?: throw ModelNotFoundException(
            "notice",
            "이 계정(id: ${noticeId})은 존재하지 않습니다."
        )

        if (user.role.name != "USER" && user.role.name != "OWNER" && user.role.name != "ADMIN") {
            throw ImmutableUserOrUnauthorizedUserException("이 공지사항을 조회할 권한이 없습니다.")
        }

        return NoticeResponse.from(notice)
    }

    @Transactional
    override fun createNotice(req: NoticeRequest, userId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        if (user.role.name != "ADMIN") {
            throw ImmutableUserOrUnauthorizedUserException("이 공지사항을 작성할 권한이 없습니다.")
        }

        noticeRepository.save(
            Notice(
                userId = user,
                title = req.title,
                content = req.content
            )
        )
        return "공지사항이 작성되었습니다."
    }

    @Transactional
    override fun updateNotice(req: NoticeRequest, userId: Long, noticeId: Long): String {

        val notice = noticeRepository.findByIdAndDeletedAt(noticeId, null) ?: throw ModelNotFoundException(
            "notice",
            "이 계정(id: ${noticeId})은 존재하지 않습니다."
        )

        checkingUser(userId, msg = "이 공지사항을 수정할 권한이 없습니다.")


        notice.title = req.title
        notice.content = req.content
        noticeRepository.save(notice)
        return "공지사항이 수정되었습니다."
    }

    @Transactional
    override fun deleteNotice(noticeId: Long, userId: Long): String {

        val notice = noticeRepository.findByIdAndDeletedAt(noticeId, null) ?: throw ModelNotFoundException(
            "notice",
            "이 계정(id: ${noticeId})은 존재하지 않습니다."
        )
        checkingUser(userId, msg = "이 공지사항을 삭제할 권한이 없습니다.")

        noticeRepository.delete(notice)
        return "공지사항이 삭제되었습니다."
    }

    private fun checkingUser(userId: Long, msg: String) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        if (user.role.name != "ADMIN") {
            throw ImmutableUserOrUnauthorizedUserException(msg)
        }
    }

}
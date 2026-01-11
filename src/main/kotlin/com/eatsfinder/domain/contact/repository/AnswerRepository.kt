package com.eatsfinder.domain.contact.repository

import com.eatsfinder.domain.contact.model.Answers
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<Answers, Long> {
}
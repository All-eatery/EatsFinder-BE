package com.eatsfinder.domain.contact.repository

import com.eatsfinder.domain.contact.model.Queries
import org.springframework.data.jpa.repository.JpaRepository

interface QueryRepository : JpaRepository<Queries, Long> {
}
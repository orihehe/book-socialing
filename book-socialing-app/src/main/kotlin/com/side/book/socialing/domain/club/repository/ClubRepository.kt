package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.Club
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ClubRepository: JpaRepository<Club, Long> {

    fun findById(clubId: Long?): Optional<Club>

}

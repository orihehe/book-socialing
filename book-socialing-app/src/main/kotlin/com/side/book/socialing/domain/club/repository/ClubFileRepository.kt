package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.ClubFile
import org.springframework.data.jpa.repository.JpaRepository

interface ClubFileRepository : JpaRepository<ClubFile, Long>

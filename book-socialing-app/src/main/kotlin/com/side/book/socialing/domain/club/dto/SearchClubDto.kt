package com.side.book.socialing.domain.club.dto

import com.side.book.socialing.domain.club.entity.Club

data class SearchClubDto(
    val club: Club,
    val status: String?,
    val role: String?
)

package com.side.book.socialing.presentation.club

import com.side.book.socialing.domain.club.enum.ParticipantRole
import com.side.book.socialing.domain.user.dto.UserDto

data class ClubMemberResponse(
    val user: UserDto,
    val role: ParticipantRole
)

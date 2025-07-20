package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.enums.ParticipantRole
import com.side.book.socialing.domain.enums.ParticipantStatus
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.presentation.note.dto.OpenNotesResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NoteService(
    private val noteRepository: NoteRepository,
) {

    @Transactional(readOnly = true)
    fun getOpenNotes(userId: Long): List<OpenNotesResponse> {
        val currentDateTime = LocalDateTime.now()
        // TODO: DB 로직 구현 필요. 아직 JPA로 JOIN하는 거 이해를 제대로 못함. ㅠ
//        val notes: List<Note> = noteRepository.findOpenNotesCreatedByUserId(
//            userId = userId,
//            joinedStatus = ParticipantStatus.JOINED, // 참여 상태: JOINED
//            currentTime = currentDateTime
//        )

        // 실제 DB 호출 로직 대신 더미 데이터 생성
        val dummyNotes = mutableListOf<OpenNotesResponse>()

        // 첫 번째 더미 노트
        dummyNotes.add(
            OpenNotesResponse(
                id = 101L,
                clubName = "더미 독서모임 A",
                bookName = "더미 데미안",
                bookAuthor = "더미 헤르만 헤세",
                bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                description = "더미 데이터로 채워진 '데미안' 토론 모임입니다.",
                participantList = listOf(
                    ParticipantInfoResponse(
                        participantId = 1L,
                        userId = userId, // 요청된 userId가 HOST인 것으로 가정
                        username = "더미유저_${userId}_HOST",
                        role = ParticipantRole.HOST,
                        status = ParticipantStatus.JOINED
                    ),
                    ParticipantInfoResponse(
                        participantId = 2L,
                        userId = 99L,
                        username = "더미유저_99_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    )
                ),
                startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
            )
        )

        // 두 번째 더미 노트
        dummyNotes.add(
            OpenNotesResponse(
                id = 102L,
                clubName = null, // 클럽이 없는 더미 노트
                bookName = "더미 어린 왕자",
                bookAuthor = "더미 앙투안 드 생텍쥐페리",
                bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                description = "더미 데이터로 채워진 '어린 왕자' 자유 토론",
                participantList = listOf(
                    ParticipantInfoResponse(
                        participantId = 3L,
                        userId = userId, // 요청된 userId가 MEMBER인 것으로 가정
                        username = "더미유저_${userId}_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    ),
                    ParticipantInfoResponse(
                        participantId = 4L,
                        userId = 98L,
                        username = "더미유저_98_HOST",
                        role = ParticipantRole.HOST,
                        status = ParticipantStatus.JOINED
                    )
                ),
                startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
            )
        )


        // ===== 세 번째 더미 노트 =====
        dummyNotes.add(
            OpenNotesResponse(
                id = 103L,
                clubName = "더미 SF 클럽",
                bookName = "더미 은하수를 여행하는 히치하이커를 위한 안내서",
                bookAuthor = "더미 더글러스 애덤스",
                bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788970135472.jpg",
                description = "더미 SF 소설 토론 모임입니다.",
                participantList = listOf(
                    ParticipantInfoResponse(
                        participantId = 5L,
                        userId = 97L,
                        username = "더미유저_97_HOST",
                        role = ParticipantRole.HOST,
                        status = ParticipantStatus.JOINED
                    ),
                    ParticipantInfoResponse(
                        participantId = 6L,
                        userId = userId,
                        username = "더미유저_${userId}_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    ),
                    ParticipantInfoResponse(
                        participantId = 7L,
                        userId = 96L,
                        username = "더미유저_96_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    )
                ),
                startDateTime = LocalDateTime.of(2025, 9, 1, 10, 0),
                endDateTime = LocalDateTime.of(2025, 9, 1, 12, 0),
            )
        )

        // ===== 네 번째 더미 노트 =====
        dummyNotes.add(
            OpenNotesResponse(
                id = 104L,
                clubName = "더미 자기계발 그룹",
                bookName = "더미 역행자",
                bookAuthor = "더미 자청",
                bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788901272580.jpg",
                description = "더미 인생을 변화시키는 독서 모임",
                participantList = listOf(
                    ParticipantInfoResponse(
                        participantId = 8L,
                        userId = userId,
                        username = "더미유저_${userId}_HOST",
                        role = ParticipantRole.HOST,
                        status = ParticipantStatus.JOINED
                    )
                ),
                startDateTime = LocalDateTime.of(2025, 9, 10, 20, 0),
                endDateTime = LocalDateTime.of(2025, 9, 10, 22, 0),
            )
        )

        // ===== 다섯 번째 더미 노트 =====
        dummyNotes.add(
            OpenNotesResponse(
                id = 105L,
                clubName = null,
                bookName = "더미 코스모스",
                bookAuthor = "더미 칼 세이건",
                bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788983711892.jpg",
                description = "더미 우주에 대한 탐구와 토론",
                participantList = listOf(
                    ParticipantInfoResponse(
                        participantId = 9L,
                        userId = 95L,
                        username = "더미유저_95_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    ),
                    ParticipantInfoResponse(
                        participantId = 10L,
                        userId = userId,
                        username = "더미유저_${userId}_MEMBER",
                        role = ParticipantRole.MEMBER,
                        status = ParticipantStatus.JOINED
                    )
                ),
                startDateTime = LocalDateTime.of(2025, 9, 20, 16, 0),
                endDateTime = LocalDateTime.of(2025, 9, 20, 18, 0),
            )
        )

        return dummyNotes
    }
}

package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.entity.Note
import com.side.book.socialing.domain.note.entity.NoteFile
import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantRole
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import com.side.book.socialing.domain.note.event.NoteCreatedEvent
import com.side.book.socialing.domain.note.event.NoteJoinedEvent
import com.side.book.socialing.domain.note.repository.NoteFileRepository
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.presentation.note.dto.ClubNotesGroupResponse
import com.side.book.socialing.presentation.note.dto.ClubNotesPageResponse
import com.side.book.socialing.presentation.note.dto.CommonNoteResponse
import com.side.book.socialing.presentation.note.dto.OpenNoteResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths
import java.time.LocalDateTime

@Service
class NoteService(
    private val noteRepository: NoteRepository,
    private val noteFileRepository: NoteFileRepository,
    private val clubRepository: ClubRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val fileUploader: FileUploader,
    private val applicationEventPublisher: ApplicationEventPublisher,

    @Value("\${file.note-dir}") private val filePath: String
) {
    /**
     * 새로운 노트를 생성하고 관련된 파일 및 참여자 정보를 함께 저장합니다.
     *
     * @param cmd 노트 생성에 필요한 모든 정보(노트 내용, 이미지 파일, 작성자 ID)가 담긴 커맨드 객체.
     * @return 성공적으로 생성되고 저장된 `Note` 엔티티.
     * @throws Exception 파일 업로드 실패 또는 데이터베이스 저장 실패 시 발생할 수 있습니다.
     */
    @Transactional
    fun createNote(cmd: CreateNoteCommand): Note {
        val club = cmd.clubId?.let { id ->
            clubRepository.findById(id).orElseThrow { IllegalArgumentException("Club not found: $id") }
        }

        // Note.create에 찾아온 club 객체를 전달
        val note = Note.create(cmd, club)
        noteRepository.save(note)
        applicationEventPublisher.publishEvent(NoteCreatedEvent(note.id!!, note.bookName, cmd.userId))

        val hostParticipant = NoteParticipant.create(
            note = note,
            userId = cmd.userId,
            role = ParticipantRole.HOST,
            status = ParticipantStatus.JOINED
        )
        noteParticipantRepository.save(hostParticipant)

        val noteFilePath = Paths.get(filePath, note.id!!.toString()).toString()
        val uploadedFiles = mutableListOf<StoredFile>()

        try {
            for (file in cmd.imageFiles) {
                val storedFile = fileUploader.upload(file, noteFilePath)
                uploadedFiles.add(storedFile)

                val noteFile = NoteFile.create(
                    note = note,
                    originalFileName = storedFile.originalFileName,
                    storedFileName = storedFile.storedFileName,
                    filePath = storedFile.filePath,
                    fileSize = file.size
                )
                noteFileRepository.save(noteFile)
            }
        } catch (e: Exception) {
            for (file in uploadedFiles) {
                fileUploader.delete(file.filePath)
            }
            throw e
        }
        return note
    }

    @Transactional
    fun joinNote(userId: Long, noteId: Long) {
        val note = noteRepository.findById(noteId)
            .orElseThrow { throw EntityNotFoundException("Note not found with id: $noteId") }

        noteParticipantRepository.findByNoteIdAndUserId(noteId, userId)?.let {
            throw IllegalStateException("User $userId is already a participant in note $noteId")
        }

        val participant = NoteParticipant.create(
            note = note,
            userId = userId,
            role = ParticipantRole.MEMBER,
            status = ParticipantStatus.JOINED
        )

        noteParticipantRepository.save(participant)

        applicationEventPublisher.publishEvent(
            NoteJoinedEvent(
                noteId = noteId,
                userId = userId
            )
        )
    }

    /**
     * 특정 사용자가 참여하고 있는 모든 노트 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 참여 중인 노트 정보가 담긴 `OpenNoteResponse` DTO 리스트.
     *         만약 참여 중인 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getOpenNotes(userId: Long, pageSize: Int, offset: Int): ClubNotesPageResponse<OpenNoteResponse> {
        val pageIndex = offset / pageSize

        // 2) Service에서 Pageable 생성 (정렬 강제)
        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.asc("endDate"), Sort.Order.desc("id"))
        )

        val now = LocalDateTime.now()
        val totalCount = noteRepository.countActiveNotesByUserId(userId, now)

        // 만약 전체 노트가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubNotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findActiveNotesByUserId(userId, now, pageable)
        val groupedByClub = notes.groupBy { it.club?.id }

        val groups = mutableListOf<ClubNotesGroupResponse<OpenNoteResponse>>()

        // 클럽 있는 노트들
        groupedByClub.filterKeys { it != null }.forEach { (clubId, list) ->
            val clubName = list.first().club!!.clubName
            groups.add(
                ClubNotesGroupResponse(
                    clubId = clubId,
                    clubName = clubName,
                    notes = list.map(::toOpenNoteResponse)
                )
            )
        }

        // 클럽 없는 노트들 (옵션: clubId = 0L, clubName = "NO_CLUB")
        groupedByClub[null]?.let { list ->
            groups.add(
                0, // 맨 앞
                ClubNotesGroupResponse(
                    clubId = null,
                    clubName = "NONE",
                    notes = list.map(::toOpenNoteResponse)
                )
            )
        }

        return ClubNotesPageResponse(totalCount = totalCount, groups = groups)
    }

    /**
     * 특정 사용자가 생성한 모든 노트 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 생성한 노트 정보가 담긴 `CreatedNoteResponse` DTO 리스트.
     *         만약 생성한 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getCreatedNotes(userId: Long, pageSize: Int, offset: Int): ClubNotesPageResponse<CommonNoteResponse> {
        val pageIndex = offset / pageSize

        // 2) Service에서 Pageable 생성 (정렬 강제)
        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.asc("endDate"), Sort.Order.desc("id"))
        )

        val now = LocalDateTime.now()
        val totalCount = noteRepository.countCreatedNotesByUserId(userId, now)

        // 만약 전체 노트가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubNotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findCreatedNotesByUserId(userId, now, pageable)

        val groupedByClub = notes.groupBy { it.club?.id }

        val groups = mutableListOf<ClubNotesGroupResponse<CommonNoteResponse>>()

        // 클럽 있는 노트들
        groupedByClub.filterKeys { it != null }.forEach { (clubId, list) ->
            val clubName = list.first().club!!.clubName
            groups.add(
                ClubNotesGroupResponse(
                    clubId = clubId,
                    clubName = clubName,
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        // 클럽 없는 노트들 (옵션: clubId = 0L, clubName = "NO_CLUB")
        groupedByClub[null]?.let { list ->
            groups.add(
                0, // 맨 앞
                ClubNotesGroupResponse(
                    clubId = null,
                    clubName = "NONE",
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        return ClubNotesPageResponse(totalCount = totalCount, groups = groups)
    }

    /**
     * 특정 사용자가 신청한 모든 노트 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 신청한 노트 정보가 담긴 `PendingNoteResponse` DTO 리스트.
     *         만약 신청한 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getPendingNotes(userId: Long, pageSize: Int, offset: Int): ClubNotesPageResponse<CommonNoteResponse> {
        val pageIndex = offset / pageSize

        // 2) Service에서 Pageable 생성 (정렬 강제)
        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.unsorted()
        )

        val now = LocalDateTime.now()
        val totalCount = noteRepository.countPendingNotesByUserId(userId, now)

        // 만약 전체 노트가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubNotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findPendingNotesByUserId(userId, now, pageable)

        val groupedByClub = notes.groupBy { it.club?.id }

        val groups = mutableListOf<ClubNotesGroupResponse<CommonNoteResponse>>()

        // 클럽 있는 노트들
        groupedByClub.filterKeys { it != null }.forEach { (clubId, list) ->
            val clubName = list.first().club!!.clubName
            groups.add(
                ClubNotesGroupResponse(
                    clubId = clubId,
                    clubName = clubName,
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        // 클럽 없는 노트들 (옵션: clubId = 0L, clubName = "NO_CLUB")
        groupedByClub[null]?.let { list ->
            groups.add(
                0, // 맨 앞
                ClubNotesGroupResponse(
                    clubId = null,
                    clubName = "NONE",
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        return ClubNotesPageResponse(totalCount = totalCount, groups = groups)
    }

    /**
     * 추천 노트 목록을 반환합니다.
     *
     * @return 추천 노트 정보가 담긴 `RecommendNoteResponse` DTO 리스트.
     *         만약 추천 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getRecommendNotes(userId: Long): List<CommonNoteResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findRecommendNotesByUserId(userId, LocalDateTime.now())

        return notes.map { note ->
            // 대표 이미지 경로
            val bookImageUrl = note.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            CommonNoteResponse(
                id = note.id!!,
                clubName = note.club?.clubName,
                bookName = note.bookName,
                bookImageUrl = bookImageUrl,
                startDateTime = note.startDate,
                endDateTime = note.endDate
            )
        }
    }

    /**
     * 퇴고한 노트 목록을 반환합니다.
     *
     * @return 추천 노트 정보가 담긴 `RevisedNoteResponse` DTO 리스트.
     *         만약 추천 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getRevisedNotes(userId: Long, pageSize: Int, offset: Int): ClubNotesPageResponse<CommonNoteResponse> {
        val pageIndex = offset / pageSize

        // 2) Service에서 Pageable 생성 (정렬 강제)
        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("endDate"), Sort.Order.desc("id"))
        )

        val now = LocalDateTime.now()
        val totalCount = noteRepository.countRevisedNotesByUserId(userId, now)

        // 만약 전체 노트가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubNotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findRevisedNotesByUserId(userId, now, pageable)

        val groupedByClub = notes.groupBy { it.club?.id }

        val groups = mutableListOf<ClubNotesGroupResponse<CommonNoteResponse>>()

        // 클럽 있는 노트들
        groupedByClub.filterKeys { it != null }.forEach { (clubId, list) ->
            val clubName = list.first().club!!.clubName
            groups.add(
                ClubNotesGroupResponse(
                    clubId = clubId,
                    clubName = clubName,
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        // 클럽 없는 노트들 (옵션: clubId = 0L, clubName = "NO_CLUB")
        groupedByClub[null]?.let { list ->
            groups.add(
                0, // 맨 앞
                ClubNotesGroupResponse(
                    clubId = null,
                    clubName = "NONE",
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        return ClubNotesPageResponse(totalCount = totalCount, groups = groups)
    }

    private fun toOpenNoteResponse(note: Note): OpenNoteResponse {
        val participants = note.participants.map {
            ParticipantInfoResponse(
                participantId = it.id!!,
                userId = it.userId,
                role = it.role.name,
                status = it.status.name
            )
        }
        val bookImageUrl = note.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

        return OpenNoteResponse(
            id = note.id!!,
            bookName = note.bookName,
            bookAuthor = note.bookAuthor,
            bookImageUrl = bookImageUrl,
            description = note.description.orEmpty(),
            participants = participants,
            startAt = note.startDate,
            endAt = note.endDate
        )
    }

    private fun toCommonNoteResponse(note: Note): CommonNoteResponse {
        val bookImageUrl = note.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

        return CommonNoteResponse(
            id = note.id!!,
            clubName = note.club?.clubName,
            bookName = note.bookName,
            bookImageUrl = bookImageUrl,
            startDateTime = note.startDate,
            endDateTime = note.endDate
        )
    }
}

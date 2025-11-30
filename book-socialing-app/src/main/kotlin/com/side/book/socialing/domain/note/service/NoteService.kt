package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.command.UpdateNoteCommand
import com.side.book.socialing.domain.note.dto.SearchNoteDto
import com.side.book.socialing.domain.note.entity.Note
import com.side.book.socialing.domain.note.entity.NoteFile
import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import com.side.book.socialing.domain.note.event.NoteCreatedEvent
import com.side.book.socialing.domain.note.repository.NoteFileRepository
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.domain.user.service.UserService
import com.side.book.socialing.global.error.exception.ForbiddenException
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.presentation.note.dto.ClubNotesGroupResponse
import com.side.book.socialing.presentation.note.dto.ClubNotesPageResponse
import com.side.book.socialing.presentation.note.dto.CommonNoteResponse
import com.side.book.socialing.presentation.note.dto.DateNotesGroupResponse
import com.side.book.socialing.presentation.note.dto.GetNoteResponse
import com.side.book.socialing.presentation.note.dto.NotesPageResponse
import com.side.book.socialing.presentation.note.dto.OpenNoteResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import com.side.book.socialing.presentation.note.dto.SearchNoteResponse
import jakarta.persistence.EntityNotFoundException
import org.apache.commons.lang3.StringUtils
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
    private val userService: UserService,

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
    fun createNote(cmd: CreateNoteCommand): Long {
        val club = cmd.clubId?.let { id ->
            clubRepository.findByIdAndDeletedFalse(id) ?: throw IllegalArgumentException("Club not found: $id")
        }

        // 클럽 정보가 존재하는 경우 클럽 호스트 권한 확인
        club?.run {
            if (!isHost(cmd.userId)) {
                throw IllegalStateException("User ${cmd.userId} is not a host of club $id")
            }
        }

        // Note.create에 찾아온 club 객체를 전달
        val note = Note.create(cmd, club)
        noteRepository.save(note)
        applicationEventPublisher.publishEvent(NoteCreatedEvent(note.id!!, note.bookName, cmd.userId))

        val hostParticipant = NoteParticipant.createHost(
            note = note,
            userId = cmd.userId
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

        return note.id!!
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
            Sort.by(Sort.Order.asc("endAt"), Sort.Order.desc("id"))
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
            Sort.by(Sort.Order.asc("endAt"), Sort.Order.desc("id"))
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
    fun getRecommendNotes(userId: Long, pageSize: Int, offset: Int): ClubNotesPageResponse<CommonNoteResponse> {
        val pageIndex = offset / pageSize

        // 2) Service에서 Pageable 생성 (정렬 강제)
        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.unsorted()
        )

        val now = LocalDateTime.now()
        val totalCount = noteRepository.countRecommendNotesByUserId(userId, now)

        // 만약 전체 노트가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubNotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findRecommendNotesByUserId(userId, now, pageable)

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
            Sort.by(Sort.Order.desc("endAt"), Sort.Order.desc("id"))
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
        val participants = note.participants.filter { it.status == ParticipantStatus.JOINED }.map {
            ParticipantInfoResponse(
                participantId = it.id!!,
                userId = it.userId,
                role = it.role.name,
                status = it.status.name
            )
        }
        val bookImageUrl = note.files.firstOrNull { !it.deleted }?.filePath
            ?: "/images/default_book_image.jpg"

        return OpenNoteResponse(
            id = note.id!!,
            bookName = note.bookName,
            bookAuthor = note.bookAuthor,
            bookImageUrl = bookImageUrl,
            description = note.description.orEmpty(),
            participants = participants,
            startAt = note.startAt,
            endAt = note.endAt
        )
    }

    private fun toCommonNoteResponse(note: Note): CommonNoteResponse {
        val bookImageUrl = note.files.firstOrNull { !it.deleted }?.filePath
            ?: "/images/default_book_image.jpg"

        return CommonNoteResponse(
            id = note.id!!,
            clubName = note.club?.clubName,
            bookName = note.bookName,
            bookImageUrl = bookImageUrl,
            startAt = note.startAt,
            endAt = note.endAt
        )
    }

    /**
     * 특정 노트 정보를 반환합니다.
     *
     * @return 노트 정보가 담긴 `GetNoteResponse` DTO 리스트.
     *         만약 노트가 없으면 null를 반환합니다.
     */
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    fun getNoteById(noteId: Long, userId: Long): GetNoteResponse {
        // 권한 확인
        val hasAccess = checkNoteAccess(noteId, userId)
        if (!hasAccess) {
            throw ForbiddenException("사용자 $userId 는 노트 $noteId 에 접근할 권한이 없거나 해당 노트를 찾을 수 없습니다.")
        }

        // 노트 조회
        val note = noteRepository.findById(noteId)
            .orElseThrow { IllegalArgumentException("Note with ID $noteId not found") }

        // 참여자 목록 정제
        val participants = note.participants
            .filter { it.status == ParticipantStatus.JOINED }
            .map {
                ParticipantInfoResponse(
                    participantId = it.id!!,
                    userId = it.userId,
                    role = it.role.name,
                    status = it.status.name
                )
            }

        // 이미지 URL 목록 정제
        val imageUrls: List<String> = note.files
            .filter { !it.deleted } // 1. 삭제 안 된 파일만 골라내기
            .map { it.filePath } // 2. 경로(String)로 변환
            .ifEmpty { // 3. 다 거르고 났는데 비어있다면? (혹은 애초에 없었다면)
                listOf("/images/default_book_image.jpg") // 기본 이미지 반환
            }

        return GetNoteResponse(
            id = note.id!!,
            clubId = note.club?.id,
            clubName = note.club?.clubName,
            bookName = note.bookName,
            bookAuthor = note.bookAuthor,
            description = note.description,
            imageUrls = imageUrls,
            participants = participants,
            startAt = note.startAt,
            endAt = note.endAt
        )
    }

    /**
     * 노트 접근 권한을 확인하는 헬퍼 메소드
     */
    private fun checkNoteAccess(noteId: Long, userId: Long): Boolean {
        return noteParticipantRepository.existsByNoteIdAndUserIdAndStatus(noteId, userId, ParticipantStatus.JOINED)
    }

    @Transactional
    fun deleteNote(noteId: Long, userId: Long) {
        val note = noteRepository.findByIdAndDeletedFalse(noteId)
            ?: throw EntityNotFoundException("Note with ID $noteId not found")

        note.delete(userId)
        note.participants.forEach { it.delete() }
        note.files.filter { !it.deleted }.forEach { it.delete() }
    }

    /**
     * 노트를 수정하고 관련된 파일 정보를 업데이트합니다.
     *
     * @param cmd 노트 수정에 필요한 모든 정보(노트 ID, 작성자 ID, 노트 내용, 이미지 파일)가 담긴 커맨드 객체.
     * @throws EntityNotFoundException 해당 ID의 노트를 찾을 수 없을 때 발생합니다.
     * @throws ForbiddenException 사용자가 노트의 호스트가 아닐 경우 발생합니다.
     */
    @Transactional
    fun updateNote(cmd: UpdateNoteCommand) {
        val note = noteRepository.findByIdAndDeletedFalse(cmd.noteId)
            ?: throw EntityNotFoundException("Note with ID ${cmd.noteId} not found")

        note.update(cmd)

        val noteFilePath = Paths.get(filePath, note.id!!.toString()).toString()
        note.files.forEach {
            it.delete()
        }

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
    }

    /**
     * 노트 이름(bookName)으로 노트를 검색하고 페이징 처리된 결과를 반환합니다.
     *
     * @param query 검색어. 노트의 책 이름 포함된 문자열로 검색됩니다.
     * @param pageSize 한 페이지에 보여줄 노트의 수.
     * @param offset 페이징을 위한 오프셋 (페이지 번호에 기반하여 계산됨).
     * @return 검색 결과에 해당하는 노트 정보가 담긴 `NotesPageResponse` DTO.
     *         검색 결과가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun searchNote(userId: Long?, query: String, pageSize: Int, offset: Int): NotesPageResponse<SearchNoteResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("endAt")) // 퇴고일 내림차순 정렬
        )

        val keywordParam = if (query.isBlank()) {
            null // keyword가 비어있으면 null을 전달
        } else {
            "%$query%" // keyword가 있으면 %를 붙여서 전달
        }

        val totalCount = noteRepository.countNoteByBookName(keywordParam)

        // 만약 검색 결과가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return NotesPageResponse(totalCount = 0L, groups = emptyList())
        }

        val rows: List<SearchNoteDto> = noteRepository.findNoteByBookName(userId, keywordParam, pageable)

        val groups = rows.map { row ->
            val n = row.note
            val bookImageUrl = n.files.firstOrNull { !it.deleted }?.filePath
                ?: "/images/default_book_image.jpg"

            SearchNoteResponse(
                id = n.id!!,
                clubName = n.club?.clubName,
                bookName = n.bookName,
                bookImageUrl = bookImageUrl,
                startAt = n.startAt,
                endAt = n.endAt,
                status = row.status,
                role = row.role
            )
        }

        return NotesPageResponse(totalCount = totalCount, groups = groups)
    }

    fun getUsers(noteId: Long): List<UserDto> {
        val participants = noteParticipantRepository.findAllByNoteId(noteId)
        return participants.mapNotNull { userService.getUser(it.userId) }
    }

    /**
     * 특정 사용자가 참여한 모든 노트 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @param dateType 날짜 검색 조건
     * @param startDate 날짜 검색 시작일
     * @param endDate 날짜 검색 종료일
     * @return 사용자가 참여 중인 노트 정보가 담긴 `CommonNoteResponse` DTO 리스트.
     *         만약 참여한 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getParticipatedNotes(userId: Long, dateType: String, startDate: LocalDateTime?, endDate: LocalDateTime?): List<DateNotesGroupResponse<CommonNoteResponse>> {
        // dateType 유효성 검사
        if (!StringUtils.equals(dateType, "START") && !StringUtils.equals(dateType, "END")) {
            // 유효하지 않은 dateType일 경우 예외 발생
            throw IllegalArgumentException("Invalid dateType parameter. Must be 'START' or 'END'.")
        }

        // dateType 유효성 검사
        if (!StringUtils.equals(dateType, "START") && !StringUtils.equals(dateType, "END")) {
            // 유효하지 않은 dateType일 경우 예외 발생
            throw IllegalArgumentException("Invalid dateType parameter. Must be 'START' or 'END'.")
        }

        val notes = noteRepository.findParticipatedNotesByUserId(userId, dateType, startDate, endDate)

        val groupedByDate = notes.groupBy { note ->
            if (StringUtils.equals(dateType, "START")) {
                note.startAt.toLocalDate()
            } else {
                note.endAt.toLocalDate()
            }
        }

        val groups = mutableListOf<DateNotesGroupResponse<CommonNoteResponse>>()

        groupedByDate.filterKeys { it != null }.forEach { (date, list) ->
            groups.add(
                DateNotesGroupResponse(
                    date = date,
                    notes = list.map(::toCommonNoteResponse)
                )
            )
        }

        groups.sortBy { it.date }

        return groups
    }
}

package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.entity.Note
import com.side.book.socialing.domain.note.entity.NoteFile
import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantRole
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import com.side.book.socialing.domain.note.repository.NoteFileRepository
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.presentation.note.dto.OpenNoteResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoteService(
    private val noteRepository: NoteRepository,
    private val noteFileRepository: NoteFileRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val fileUploader: FileUploader,

    @Value("\${file.note-dir}") private val filePath: String
)
{
    /**
     * 새로운 노트를 생성하고 관련된 파일 및 참여자 정보를 함께 저장합니다.
     *
     * @param cmd 노트 생성에 필요한 모든 정보(노트 내용, 이미지 파일, 작성자 ID)가 담긴 커맨드 객체.
     * @return 성공적으로 생성되고 저장된 `Note` 엔티티.
     * @throws Exception 파일 업로드 실패 또는 데이터베이스 저장 실패 시 발생할 수 있습니다.
     */
    @Transactional
    fun createNote(cmd: CreateNoteCommand): Note {
        val note = Note.create(cmd)
        noteRepository.save(note)

        val hostParticipant = NoteParticipant.create(
            note = note,
            userId = cmd.userId,
            role = ParticipantRole.HOST,
            status = ParticipantStatus.JOINED
        )
        noteParticipantRepository.save(hostParticipant)

        val noteFilePath = filePath + note.id!!
        val uploadedFiles = mutableListOf<StoredFile>()

        try {
            for((_, file) in cmd.imageFiles.withIndex()) {
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

    /**
     * 특정 사용자가 참여하고 있는 모든 노트 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 참여 중인 노트 정보가 담긴 `OpenNoteResponse` DTO 리스트.
     *         만약 참여 중인 노트가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getOpenNotes(userId: Long): List<OpenNoteResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val notes = noteRepository.findActiveNotesByUserId(userId)

        return notes.map { note ->
            val participantInfos = note.participants.map { participant ->
                ParticipantInfoResponse(
                    participantId = participant.id!!,
                    userId = participant.userId,
                    role = participant.role.name,
                    status = participant.status.name
                )
            }

            // 대표 이미지 경로
            val bookImageUrl = note.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            OpenNoteResponse(
                id = note.id!!,
                clubName = "saisai", // TODO: note.club?.name 으로 실제 클럽 이름 가져오기
                bookName = note.bookName,
                bookAuthor = note.bookAuthor,
                bookImageUrl = bookImageUrl,
                description = note.description ?: "",
                participantList = participantInfos,
                startDateTime = note.startDate,
                endDateTime = note.endDate
            )
        }
    }
}

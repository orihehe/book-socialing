package com.side.book.socialing.domain.club.service

import com.side.book.socialing.domain.club.repository.ClubFileRepository
import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.global.file.FileUploader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubFileRepository: ClubFileRepository,
    private val clubParticipantRepository: ClubParticipantRepository,
    private val fileUploader: FileUploader,

    @Value("\${file.note-dir}") private val filePath: String
)
{

}

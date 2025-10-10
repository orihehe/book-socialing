package com.side.book.socialing.domain.club.service

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.club.entity.Club
import com.side.book.socialing.domain.club.entity.ClubFile
import com.side.book.socialing.domain.club.entity.ClubParticipant
import com.side.book.socialing.domain.club.enum.ParticipantRole
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import com.side.book.socialing.domain.club.repository.ClubFileRepository
import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.presentation.club.dto.CommonClubResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubFileRepository: ClubFileRepository,
    private val clubParticipantRepository: ClubParticipantRepository,
    private val fileUploader: FileUploader,

    @Value("\${file.club-dir}") private val filePath: String
) {
    /**
     * 새로운 클럽을 생성하고 관련된 파일 및 참여자 정보를 함께 저장합니다.
     *
     * @param cmd 클럽 생성에 필요한 모든 정보(클럽명, 소개, 이미지 파일, 작성자 ID)가 담긴 커맨드 객체.
     * @return 성공적으로 생성되고 저장된 `Club` 엔티티.
     * @throws Exception 파일 업로드 실패 또는 데이터베이스 저장 실패 시 발생할 수 있습니다.
     */
    @Transactional
    fun createClub(cmd: CreateClubCommand): Club {
        val club = Club.create(cmd)
        clubRepository.save(club)

        val clubFilePath = filePath + club.id!!
        val uploadedFiles = mutableListOf<StoredFile>()

        val hostParticipant = ClubParticipant.create(
            club = club,
            userId = cmd.userId,
            role = ParticipantRole.HOST,
            status = ParticipantStatus.JOINED
        )
        clubParticipantRepository.save(hostParticipant)

        try {
            for (file in cmd.imageFiles) {
                val storedFile = fileUploader.upload(file, clubFilePath)
                uploadedFiles.add(storedFile)

                val clubFile = ClubFile.create(
                    club = club,
                    originalFileName = storedFile.originalFileName,
                    storedFileName = storedFile.storedFileName,
                    filePath = storedFile.filePath,
                    fileSize = file.size
                )
                clubFileRepository.save(clubFile)
            }
        } catch (e: Exception) {
            for (file in uploadedFiles) {
                fileUploader.delete(file.filePath)
            }
            throw e
        }
        return club
    }

    /**
     * 특정 사용자가 참여하고 있는 모든 클럽 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 참여 중인 클럽 정보가 담긴 `ClubResponse` DTO 리스트.
     *         만약 참여 중인 클럽이 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getJoinedClubs(userId: Long): List<CommonClubResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findJoinedClubByUserId(userId)

        return clubs.map { club ->
            // 대표 이미지 경로
            val clubImageUrl = club.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrl = clubImageUrl,
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }
    }

    /**
     * 특정 사용자가 생성한 모든 클럽 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 생성한 클럽 정보가 담긴 `CreatedClubResponse` DTO 리스트.
     *         만약 생성한 클럽가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getCreatedClubs(userId: Long): List<CommonClubResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findCreatedClubsByUserId(userId)

        return clubs.map { club ->
            // 대표 이미지 경로
            val clubImageUrl = club.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrl = clubImageUrl,
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }
    }

    /**
     * 특정 사용자가 신청한 모든 클럽 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 신청한 클럽 정보가 담긴 `PendingClubResponse` DTO 리스트.
     *         만약 신청한 클럽가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getPendingClubs(userId: Long): List<CommonClubResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findPendingClubsByUserId(userId)

        return clubs.map { club ->
            // 대표 이미지 경로
            val clubImageUrl = club.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrl = clubImageUrl,
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }
    }

    @Transactional(readOnly = true)
    fun getClubMemberIds(clubId: Long): Set<Long> {
        return clubParticipantRepository.findAllByClubId(clubId).map { it.userId }.toSet()
    }

    /**
     * 추천 클럽 목록을 반환합니다.
     *
     * @return 추천 클럽 정보가 담긴 `RecommendClubResponse` DTO 리스트.
     *         만약 추천 클럽가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getRecommendClubs(userId: Long): List<CommonClubResponse> {
        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findRecommendClubsByUserId(userId)

        return clubs.map { club ->
            // 대표 이미지 경로
            val clubImageUrl = club.files.firstOrNull()?.filePath ?: "/images/default_book_image.jpg"

            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrl = clubImageUrl,
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }
    }
}

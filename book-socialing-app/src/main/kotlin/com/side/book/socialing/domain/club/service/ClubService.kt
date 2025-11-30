package com.side.book.socialing.domain.club.service

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.club.command.UpdateClubCommand
import com.side.book.socialing.domain.club.dto.SearchClubDto
import com.side.book.socialing.domain.club.entity.Club
import com.side.book.socialing.domain.club.entity.ClubFile
import com.side.book.socialing.domain.club.entity.ClubParticipant
import com.side.book.socialing.domain.club.enum.ParticipantRole
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import com.side.book.socialing.domain.club.repository.ClubFileRepository
import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.global.error.exception.ForbiddenException
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.presentation.club.dto.ClubPageResponse
import com.side.book.socialing.presentation.club.dto.CommonClubResponse
import com.side.book.socialing.presentation.club.dto.SearchClubResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths

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
    fun getJoinedClubs(userId: Long, pageSize: Int, offset: Int): ClubPageResponse<CommonClubResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("createdAt"))
        )

        val totalCount = clubRepository.countJoinedClubByUserId(userId)

        // 만약 목록이 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findJoinedClubByUserId(userId, pageable)

        val groups = clubs.map { club ->
            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrls = club.files.map { it.filePath },
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }

        return ClubPageResponse(totalCount = totalCount, groups = groups)
    }

    /**
     * 특정 사용자가 생성한 모든 클럽 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 생성한 클럽 정보가 담긴 `CreatedClubResponse` DTO 리스트.
     *         만약 생성한 클럽가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getCreatedClubs(userId: Long, pageSize: Int, offset: Int): ClubPageResponse<CommonClubResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("createdAt"))
        )

        val totalCount = clubRepository.countCreatedClubsByUserId(userId)

        // 만약 목록이 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findCreatedClubsByUserId(userId, pageable)

        val groups = clubs.map { club ->
            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrls = club.files.map { it.filePath },
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }

        return ClubPageResponse(totalCount = totalCount, groups = groups)
    }

    /**
     * 특정 사용자가 신청한 모든 클럽 목록을 반환합니다.
     *
     * @param userId 정보를 조회할 사용자의 ID.
     * @return 사용자가 신청한 클럽 정보가 담긴 `PendingClubResponse` DTO 리스트.
     *         만약 신청한 클럽가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getPendingClubs(userId: Long, pageSize: Int, offset: Int): ClubPageResponse<CommonClubResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("createdAt"))
        )

        val totalCount = clubRepository.countPendingClubsByUserId(userId)

        // 만약 목록이 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findPendingClubsByUserId(userId, pageable)

        val groups = clubs.map { club ->
            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrls = club.files.map { it.filePath },
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }

        return ClubPageResponse(totalCount = totalCount, groups = groups)
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
    fun getRecommendClubs(userId: Long, pageSize: Int, offset: Int): ClubPageResponse<CommonClubResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.unsorted()
        )

        val totalCount = clubRepository.countRecommendClubsByUserId(userId)

        // 만약 목록이 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubPageResponse(totalCount = 0L, groups = emptyList())
        }

        // 사용자가 참여하고 있는 모든 참여 정보를 찾는다.
        val clubs = clubRepository.findRecommendClubsByUserId(userId, pageable)

        val groups = clubs.map { club ->
            CommonClubResponse(
                id = club.id!!,
                clubName = club.clubName,
                clubImageUrls = club.files.map { it.filePath },
                description = club.description ?: "",
                memberCount = club.participants.size
            )
        }

        return ClubPageResponse(totalCount = totalCount, groups = groups)
    }

    @Transactional(readOnly = true)
    fun getClubById(clubId: Long): CommonClubResponse {
        val club = clubRepository.findById(clubId)
            .orElseThrow { EntityNotFoundException("Club not found with ID: $clubId") }

        return CommonClubResponse(
            id = club.id!!,
            clubName = club.clubName,
            clubImageUrls = club.files.map { it.filePath },
            description = club.description ?: "",
            memberCount = club.participants.count { it.status == ParticipantStatus.JOINED }
        )
    }

    @Transactional
    fun deleteClub(clubId: Long, userId: Long) {
        val club = clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club with ID $clubId not found")

        club.delete(userId)
        club.participants.forEach { it.delete() }
        club.files.forEach { it.delete() }
        club.reviews.forEach { it.delete() }
    }

    /**
     * 클럽을 수정하고 관련된 파일 정보를 업데이트합니다.
     *
     * @param cmd 클럽 수정에 필요한 모든 정보(클럽 ID, 작성자 ID, 클럽 내용, 이미지 파일)가 담긴 커맨드 객체.
     * @throws EntityNotFoundException 해당 ID의 클럽를 찾을 수 없을 때 발생합니다.
     * @throws ForbiddenException 사용자가 클럽의 호스트가 아닐 경우 발생합니다.
     */
    @Transactional
    fun updateClub(cmd: UpdateClubCommand) {
        val club = clubRepository.findByIdAndDeletedFalse(cmd.clubId)
            ?: throw EntityNotFoundException("Club with ID ${cmd.clubId} not found")

        club.update(cmd)

        val clubFilePath = Paths.get(filePath, club.id!!.toString()).toString()
        club.files.forEach {
            it.delete()
        }

        val uploadedFiles = mutableListOf<StoredFile>()
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
    }

    /**
     * 클럽 이름 또는 설명으로 클럽을 검색하고 페이징 처리된 결과를 반환합니다.
     *
     * @param query 검색어. 클럽 이름 또는 설명에 포함된 문자열로 검색됩니다.
     * @param pageSize 한 페이지에 보여줄 클럽의 수.
     * @param offset 페이징을 위한 오프셋 (페이지 번호에 기반하여 계산됨).
     * @return 검색 결과에 해당하는 클럽 정보가 담긴 `ClubPageResponse` DTO.
     *         검색 결과가 없으면 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    fun searchClub(userId: Long?, query: String, pageSize: Int, offset: Int): ClubPageResponse<SearchClubResponse> {
        val pageIndex = offset / pageSize

        val pageable = PageRequest.of(
            pageIndex,
            pageSize,
            Sort.by(Sort.Order.desc("createdAt"))
        )

        val keywordParam = if (query.isBlank()) {
            null // keyword가 비어있으면 null을 전달
        } else {
            "%$query%" // keyword가 있으면 %를 붙여서 전달
        }

        val totalCount = clubRepository.countSearchClubByClubName(keywordParam)

        // 만약 검색 결과가 없다면, 빈 결과 반환
        if (totalCount == 0L) {
            return ClubPageResponse(totalCount = 0L, groups = emptyList())
        }

        val rows: List<SearchClubDto> = clubRepository.findSearchClubByClubName(userId, keywordParam, pageable)

        val groups = rows.map { row ->
            val c = row.club
            SearchClubResponse(
                id = c.id!!,
                clubName = c.clubName,
                clubImageUrls = c.files.map { it.filePath },
                description = c.description ?: "",
                memberCount = c.participants.size,
                status = row.status,
                role = row.role
            )
        }

        return ClubPageResponse(totalCount = totalCount, groups = groups)
    }
}

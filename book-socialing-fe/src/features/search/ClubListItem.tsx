import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { toast } from 'sonner'

import { BaseButton } from '@/components/common/BaseButton'
import { apiFetch } from '@/lib/api'
import { ClubSearchResult } from '@/types/club'
import { getImageUrl } from '@/util'

interface ClubListItemProps {
  club: ClubSearchResult
}

export function ClubListItem({ club }: ClubListItemProps) {
  const queryClient = useQueryClient()
  const cancelJoinClubMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/club/${club.id}/join/cancel`, {
        method: 'PATCH',
      })
    },
    onSuccess: () => {
      toast.success('클럽 신청이 취소되었습니다.')
      // 검색 결과 캐시를 무효화하여 최신 상태로 갱신
      queryClient.invalidateQueries({ queryKey: ['club-search'] })
    },
    onError: () => {
      toast.error('클럽 신청 취소에 실패했습니다.')
    },
  })

  const joinClubMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/club/${club.id}/join/request`, {
        method: 'POST',
      })

      return true
    },
    onSuccess: () => {
      toast.success('클럽 신청이 완료되었습니다.')
      // 검색 결과 캐시를 무효화하여 최신 상태로 갱신
      queryClient.invalidateQueries({ queryKey: ['club-search'] })
      queryClient.invalidateQueries({ queryKey: ['clubs'] })
    },
    onError: () => {
      toast.error('클럽 신청에 실패했습니다.')
    },
  })

  const handleCancelJoinClub = () => {
    cancelJoinClubMutation.mutate()
  }

  const handleJoinClub = () => {
    joinClubMutation.mutate()
  }

  return (
    <div className="flex items-center space-x-3 py-3 border-b border-gray-100 last:border-b-0">
      <Link to={`/club/${club.id}`} className="flex items-center space-x-3 flex-1">
        <img
          className="w-16 h-16 bg-gray-200 rounded-lg flex-shrink-0 object-cover"
          src={getImageUrl(club.clubImageUrls?.[0])}
          alt={club.clubName}
        />
        <div className="flex-1 min-w-0">
          <h3 className="font-semibold text-gray-900 text-sm mb-1">{club.clubName}</h3>
          <p className="text-xs text-gray-500 line-clamp-1">
            {club.description || '소개글이 없습니다'}
          </p>
        </div>
      </Link>
      {club.role === 'HOST' ? (
        <BaseButton asChild>
          <Link to={`/club/${club.id}/members`}>관리</Link>
        </BaseButton>
      ) : club.status === 'JOINED' ? (
        <BaseButton asChild>
          <Link to={`/club/${club.id}`}>보러가기</Link>
        </BaseButton>
      ) : club.status === 'PENDING_APPROVAL' ? (
        <BaseButton onClick={() => handleCancelJoinClub()}>신청취소</BaseButton>
      ) : (
        <BaseButton onClick={() => handleJoinClub()}>신청하기</BaseButton>
      )}
    </div>
  )
}

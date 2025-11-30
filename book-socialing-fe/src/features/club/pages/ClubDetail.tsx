import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ArrowUp, Pencil, UsersRound, Share2 } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'
import { toast } from 'sonner'

import { BottomButton } from '@/components/common/BottomButton'
import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { MainLayout } from '@/features/shared/MainLayout'
import { apiFetch } from '@/lib/api'
import { UserDetail } from '@/types/user'

import { ClubCarousel } from '../components/ClubCarousel'
import NoteSelector from '../components/NoteSelector'
import UserScroll from '../components/UserScroll'

export default function ClubDetail() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { id } = useParams<{ id: string }>()

  const {
    data: clubDetail,
    isLoading,
    isFetched,
  } = useQuery({
    queryKey: ['club', id],
    queryFn: async () => {
      const res = await apiFetch(`/v1/club/${id}`)
      return res.json()
    },
    enabled: !!id,
  })

  const { data: clubMembers = [] } = useQuery<UserDetail[]>({
    queryKey: ['club', id, 'members'],
    queryFn: async () => {
      const res = await apiFetch(`/v1/club/${id}/members`)
      return res.json()
    },
    enabled: !!id,
  })

  const cancelJoinClubMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/club/${id}/join/cancel`, {
        method: 'PATCH',
      })
    },
    onSuccess: () => {
      toast.success('클럽 신청이 취소되었습니다.')
    },
    onError: () => {
      toast.error('클럽 신청 취소에 실패했습니다.')
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['clubMembers', id] })
    },
  })

  const joinClubMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/club/${id}/join/request`, {
        method: 'POST',
      })

      return true
    },
    onSuccess: () => {
      toast.success('클럽 신청이 완료되었습니다.')
    },
    onError: () => {
      toast.error('클럽 신청에 실패했습니다.')
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['clubs'] })
    },
  })

  const handleShare = async () => {
    const shareData = {
      title: clubDetail?.clubName || '클럽',
      text: clubDetail?.description || '클럽에 참여해보세요!',
      url: window.location.href,
    }

    try {
      // Web Share API 지원 확인
      if (navigator.share) {
        await navigator.share(shareData)
      } else {
        // Web Share API를 지원하지 않으면 URL 복사
        await navigator.clipboard.writeText(window.location.href)
        toast.success('링크가 복사되었습니다.')
      }
    } catch (error) {
      // 사용자가 공유를 취소한 경우 (AbortError)는 무시
      if (error instanceof Error && error.name !== 'AbortError') {
        toast.error('공유에 실패했습니다.')
      }
    }
  }

  if (isLoading) {
    return <LoadingPage />
  }

  if (isFetched && !clubDetail) {
    return (
      <MainLayout>
        <div className="flex flex-col items-center justify-center h-[60vh]">
          <p className="text-lg font-semibold mb-2">해당 클럽이 삭제되었거나 없습니다.</p>
          <p className="text-sm text-muted-foreground mb-6">다시 확인해 주세요.</p>
          <Button onClick={() => navigate('/club')}>클럽 목록으로 돌아가기</Button>
        </div>
      </MainLayout>
    )
  }

  return (
    <>
      <PageHeader title={clubDetail.clubName} showBack>
        <Button variant="ghost" size="icon" onClick={handleShare}>
          <Share2 />
        </Button>
      </PageHeader>
      {/* 이미지 */}
      <ClubCarousel images={clubDetail.clubImageUrls} />
      <div className="mx-4">
        {/* 클럽 소개 */}
        <div className="space-y-2 my-7">
          <Label className="text-base font-bold">클럽 소개</Label>
          <p className="text-sm text-muted-foreground leading-relaxed break-keep whitespace-pre-line mt-2">
            {clubDetail.description}
          </p>
        </div>

        {/* 멤버 */}
        <UserScroll clubMembers={clubMembers} />

        {/* 클럽 내역 */}
        <NoteSelector />
      </div>

      <div className="fixed bottom-10 right-2 flex flex-col gap-1">
        {clubDetail.role === 'HOST' && (
          <Button
            className="rounded-full w-10 h-10 shadow-lg opacity-80"
            size="icon"
            onClick={() => navigate(`/club/${id}/members`)}
          >
            <UsersRound />
          </Button>
        )}
        <Button
          className="rounded-full w-10 h-10 shadow-lg opacity-80"
          size="icon"
          onClick={() => navigate(`/club/${id}/edit`)}
        >
          <Pencil />
        </Button>
        <Button
          variant="secondary"
          className="rounded-full w-10 h-10 shadow-lg opacity-80"
          size="icon"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        >
          <ArrowUp />
        </Button>
        {!clubDetail.role &&
          (clubDetail.status === 'PENDING_APPROVAL' ? (
            <BottomButton onClick={() => cancelJoinClubMutation.mutate()}>신청취소</BottomButton>
          ) : (
            <BottomButton onClick={() => joinClubMutation.mutate()}>신청하기</BottomButton>
          ))}
      </div>
    </>
  )
}

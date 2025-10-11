import { useQuery } from '@tanstack/react-query'
import { ArrowUp, Pencil, UsersRound, Share2 } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'

import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { MainLayout } from '@/features/shared/MainLayout'
import { apiFetch } from '@/lib/api'

import { ClubCarousel } from '../components/ClubCarousel'
import NoteSelector from '../components/NoteSelector'
import UserScroll from '../components/UserScroll'

export default function ClubDetail() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const {
    data: clubDetail,
    isLoading,
    isFetched,
  } = useQuery({
    queryKey: ['club', id],
    queryFn: async () => {
      const res = await apiFetch(`/v1/club/${id}`)
      if (!res.ok) throw new Error('클럽 정보를 불러오지 못했습니다.')
      return res.json()
    },
    enabled: !!id,
  })

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
        <Button variant="ghost" size="icon">
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
        <UserScroll />

        {/* 클럽 내역 */}
        <NoteSelector />
      </div>

      <div className="fixed bottom-10 right-2 flex flex-col gap-1">
        <Button
          className="rounded-full w-10 h-10 shadow-lg opacity-80"
          size="icon"
          onClick={() => navigate(`/club/${id}/members`)}
        >
          <UsersRound />
        </Button>
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
      </div>
    </>
  )
}

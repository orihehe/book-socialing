import { useQuery } from '@tanstack/react-query'
import { ArrowUp, Pencil, UsersRound, Share2 } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'

import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { MainLayout } from '@/features/shared/MainLayout'
import { UserDetail } from '@/types/user'

import { ClubCarousel } from '../components/ClubCarousel'

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
      const res = await fetch(`/api/v1/club/${id}`)
      if (!res.ok) throw new Error('클럽 정보를 불러오지 못했습니다.')
      return res.json()
    },
    enabled: !!id,
  })

  const { data: clubMembers = [] } = useQuery<UserDetail[]>({
    queryKey: ['club', id, 'members'],
    queryFn: async () => {
      const res = await fetch(`/api/v1/club/${id}/members`)
      if (!res.ok) throw new Error('클럽 멤버 정보를 불러오지 못했습니다.')
      return res.json()
    },
    enabled: !!id,
  })

  console.log(clubDetail, clubMembers)

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
        <div className="space-y-2 my-7">
          <Label className="text-base font-bold">멤버 7</Label>
          <div
            className="overflow-x-auto mt-2"
            style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
          >
            <div className="flex gap-3 w-max overflow-x-auto !scrollbar-hide touch-auto">
              {clubMembers.map(member => (
                <div key={member.user.id} className="w-15 h-15 rounded-full bg-gray-300 shrink-0" />
              ))}
            </div>
          </div>
        </div>

        {/* 클럽 내역 */}
        <div className="space-y-2 my-7">
          <Label className="text-base font-bold">클럽 내역</Label>
          <div
            className="overflow-x-auto mt-2"
            style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
          >
            <div className="flex gap-3 w-max">
              {Array.from({ length: 7 }).map((_, i) => (
                <button key={i} className="w-15 h-15 rounded-lg bg-gray-300 shrink-0" />
              ))}
            </div>
          </div>
        </div>

        {/* 노트 상세보기 */}
      </div>

      <div className="fixed bottom-10 right-2 flex flex-col gap-1">
        <Button
          className="rounded-full w-10 h-10 shadow-lg opacity-80"
          size="icon"
          onClick={() => navigate(`/club/${id}/users`)}
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

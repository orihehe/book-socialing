import { useQuery } from '@tanstack/react-query'
import { ArrowUp, ArrowUpRightIcon, Plus } from 'lucide-react'
import { Link, useNavigate } from 'react-router-dom'

import { Button } from '@/components/ui/button'
import { Empty, EmptyDescription, EmptyHeader, EmptyTitle } from '@/components/ui/empty'
import { apiFetch } from '@/lib/api'

import { ClubSection as ClubSectionComponent } from './components/ClubSection'
import { MainLayout } from '../shared/MainLayout'

export default function ClubView() {
  const navigate = useNavigate()

  // 추천 클럽 (recommended), 내가 속한 클럽 (joined), 내가 만든 클럽 (created)
  const { data: recommendedClubs, refetch: refetchRecommended } = useQuery({
    queryKey: ['club', 'recommended'],
    queryFn: async () => {
      const res = await apiFetch('/v1/club/recommend')
      const result = await res.json()

      return { title: '추천 클럽', totalCount: result.totalCount ?? 0, clubs: result.groups ?? [] }
    },
  })

  const { data: joinedClubs } = useQuery({
    queryKey: ['club', 'joined'],
    queryFn: async () => {
      const res = await apiFetch('/v1/club/joined')
      const result = await res.json()

      return { title: '내 클럽', totalCount: result.totalCount ?? 0, clubs: result.groups ?? [] }
    },
  })

  const { data: creaetdClubs } = useQuery({
    queryKey: ['club', 'created'],
    queryFn: async () => {
      const res = await apiFetch('/v1/club/created')
      const result = await res.json()

      return {
        title: '생성한 클럽',
        totalCount: result.totalCount ?? 0,
        clubs: result.groups ?? [],
        showActions: true,
      }
    },
  })

  const noClubs =
    (!joinedClubs?.clubs || joinedClubs.clubs.length === 0) &&
    (!creaetdClubs?.clubs || creaetdClubs.clubs.length === 0) &&
    (!recommendedClubs?.clubs || recommendedClubs.clubs.length === 0)

  return (
    <MainLayout>
      {/* Main Content */}
      {noClubs ? (
        <Empty>
          <EmptyHeader>
            <EmptyTitle>아직 가입한 클럽이 없어요</EmptyTitle>
            <EmptyDescription>마음에 드는 클럽을 찾아 함께 시작해 볼까요?</EmptyDescription>
          </EmptyHeader>

          <Button variant="link" asChild className="text-muted-foreground" size="sm">
            <Link to="search">
              클럽 찾으러 가기 <ArrowUpRightIcon />
            </Link>
          </Button>
        </Empty>
      ) : (
        <div className="flex flex-col p-4 mx-auto">
          {joinedClubs && <ClubSectionComponent section={joinedClubs} />}
          {creaetdClubs && <ClubSectionComponent section={creaetdClubs} />}
          {recommendedClubs && (
            <ClubSectionComponent section={{ ...recommendedClubs, refetch: refetchRecommended }} />
          )}
        </div>
      )}

      <div className="fixed bottom-13 right-2 flex flex-col gap-1">
        <Button
          className="rounded-full w-13 h-13 shadow-lg opacity-80"
          size="icon"
          onClick={() => navigate('/club/create')}
        >
          <Plus />
        </Button>
        <Button
          variant="secondary"
          className="rounded-full w-13 h-13 shadow-lg opacity-80"
          size="icon"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        >
          <ArrowUp />
        </Button>
      </div>
    </MainLayout>
  )
}

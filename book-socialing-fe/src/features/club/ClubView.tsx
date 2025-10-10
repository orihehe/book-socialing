import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

import { BottomButton } from '@/components/common/BottomButton'

import { ClubSection as ClubSectionComponent } from './components/ClubSection'
import { MainLayout } from '../shared/MainLayout'

export default function ClubView() {
  const navigate = useNavigate()

  // 추천 클럽 (recommended), 내가 속한 클럽 (joined), 내가 만든 클럽 (created)
  const { data: recommendedClubs, refetch: refetchRecommended } = useQuery({
    queryKey: ['club', 'recommended'],
    queryFn: async () => {
      const res = await fetch('/api/v1/club/recommend')
      if (!res.ok) throw new Error('추천 클럽 정보를 불러오지 못했습니다.')

      const result = await res.json()

      return { title: '추천 클럽', totalCount: 3, clubs: result }
    },
  })

  const { data: joinedClubs } = useQuery({
    queryKey: ['club', 'joined'],
    queryFn: async () => {
      const res = await fetch('/api/v1/club/joined')
      if (!res.ok) throw new Error('가입한 클럽 정보를 불러오지 못했습니다.')
      const result = await res.json()

      return { title: '내 클럽', totalCount: 3, clubs: result }
    },
  })

  const { data: creaetdClubs } = useQuery({
    queryKey: ['club', 'created'],
    queryFn: async () => {
      const res = await fetch('/api/v1/club/created')
      if (!res.ok) throw new Error('생성한 클럽 정보를 불러오지 못했습니다.')
      const result = await res.json()

      return { title: '생성한 클럽', totalCount: 3, showActions: true, clubs: result }
    },
  })

  return (
    <MainLayout>
      {/* Main Content */}
      <div className="flex flex-col p-4 mx-auto">
        {joinedClubs && <ClubSectionComponent section={joinedClubs} />}
        {creaetdClubs && <ClubSectionComponent section={creaetdClubs} />}
        {recommendedClubs && (
          <ClubSectionComponent section={{ ...recommendedClubs, refetch: refetchRecommended }} />
        )}
      </div>

      <BottomButton onClick={() => navigate('/club/create')} children="클럽 생성하러가기" />
    </MainLayout>
  )
}

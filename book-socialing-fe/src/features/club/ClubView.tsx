import { useNavigate } from 'react-router-dom'

import { BottomButton } from '@/components/common/BottomButton'
import type { ClubSection } from '@/types/club'

import { ClubSection as ClubSectionComponent } from './components/ClubSection'
import { MainLayout } from '../shared/MainLayout'

// Mock data
const mockClubSections: ClubSection[] = [
  {
    title: '내 클럽',
    count: 3,
    showViewAll: true,
    clubs: [
      {
        id: '1',
        name: '클럽명',
        description:
          '간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄',
        memberCount: 8,
        isMyClub: true,
      },
      {
        id: '2',
        name: '클럽명',
        description:
          '간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄',
        memberCount: 12,
        isMyClub: true,
      },
    ],
  },
  {
    title: '생성한 클럽',
    count: 4,
    showActions: true,
    clubs: [
      {
        id: '3',
        name: '클럽이름',
        description: '',
        memberCount: 8,
        isCreatedByMe: true,
      },
      {
        id: '4',
        name: '클럽이름',
        description: '',
        memberCount: 15,
        isCreatedByMe: true,
      },
    ],
  },
  {
    title: '추천클럽',
    count: 10,
    clubs: [
      {
        id: '5',
        name: '클럽명',
        description:
          '간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄',
        memberCount: 8,
      },
      {
        id: '6',
        name: '클럽명',
        description:
          '간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄 간단 클럽 소개 최대 두줄',
        memberCount: 10,
      },
    ],
  },
]

export default function ClubView() {
  const navigate = useNavigate()

  return (
    <MainLayout>
      {/* Main Content */}
      <div className="flex flex-col p-4 mx-auto">
        {mockClubSections.map(section => (
          <ClubSectionComponent key={section.title} section={section} />
        ))}
      </div>

      <BottomButton onClick={() => navigate('/club/create')} children="클럽 생성하러가기" />
    </MainLayout>
  )
}

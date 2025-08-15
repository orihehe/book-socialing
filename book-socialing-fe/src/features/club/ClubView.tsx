import { CircleUserRound, Plus, Search } from 'lucide-react'

import LNB from '@/components/layout/LNB'
import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import type { ClubSection } from '@/types/club'

import { ClubSection as ClubSectionComponent } from './components/ClubSection'

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
  const lnbItems = [
    {
      name: '클럽',
      key: 'club',
      children: [],
    },
    {
      name: '노트',
      key: 'note',
      children: [
        { key: 'all', name: '전체' },
        { key: 'open', name: '열린노트' },
        { key: 'closed', name: '닫힌 노트' },
      ],
    },
  ]

  return (
    <>
      <PageHeader title="SAISAI">
        <div className="flex items-center">
          <Button variant="ghost" size="icon">
            <Search />
          </Button>
          <Button variant="ghost" className="-ml-1">
            <CircleUserRound />
          </Button>
        </div>
      </PageHeader>

      <LNB items={lnbItems} />

      {/* Main Content */}
      <div className="flex flex-col p-4 mx-auto">
        {mockClubSections.map(section => (
          <ClubSectionComponent key={section.title} section={section} />
        ))}
      </div>

      {/* Fixed Bottom Action Button */}
      <div className="fixed bottom-6 left-4 right-4">
        <Button className="w-full bg-main hover:bg-main/90 text-white py-4 text-base font-medium rounded-lg">
          <Plus className="mr-2" size={20} />
          클럽 생성하러가기
        </Button>
      </div>
    </>
  )
}

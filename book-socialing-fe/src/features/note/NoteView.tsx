import { CircleUserRound, Search } from 'lucide-react'
import { useState } from 'react'

import LNB, { type LNBItem } from '@/components/layout/LNB'
import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import type { Note } from '@/types/note'

import { CurrentNotes } from './components/CurrentNotes'
import { MyNotes } from './components/MyNotes'
import { RevisedNotes } from './components/RevisedNotes'

const dummyCurrentNotes: Note[] = [
  {
    id: '1',
    title: '두 개의 탑',
    author: 'JRR Tolkein',
    imageUrl: 'https://covers.openlibrary.org/b/id/8231856-L.jpg',
    description: '~클럽의 몇번째 책임입니다',
    startDateTime: '2025-06-22',
    endDateTime: '2025-06-28',
  },
  {
    id: '2',
    title: '세 개의 탑',
    author: 'JRR Tolkein',
    imageUrl: 'https://covers.openlibrary.org/b/id/8231856-L.jpg',
    description: '~클럽의 몇번째 책임입니다',
    startDateTime: '2025-06-22',
    endDateTime: '2025-06-29',
  },
  {
    id: '3',
    title: '네 개의 탑',
    author: 'JRR Tolkein',
    imageUrl: 'https://covers.openlibrary.org/b/id/8231856-L.jpg',
    description: '~클럽의 몇번째 책임입니다',
    startDateTime: '2025-06-22',
    endDateTime: '2025-06-30',
  },
]

export default function NoteView() {
  const [activeTab, setActiveTab] = useState('all')

  const lnbItems: LNBItem[] = [
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

      <LNB items={lnbItems} activeTab={activeTab} onTabChange={setActiveTab} />

      <div className="flex flex-col p-2 mx-auto">
        {activeTab === 'all' && (
          <>
            <CurrentNotes currentNotes={dummyCurrentNotes} />
            <MyNotes myNotes={dummyCurrentNotes} />
            <RevisedNotes revisedNotes={dummyCurrentNotes} />
          </>
        )}
        {activeTab === 'open' && (
          <>
            <CurrentNotes currentNotes={dummyCurrentNotes} />
            <MyNotes myNotes={dummyCurrentNotes} />
          </>
        )}
        {activeTab === 'closed' && <RevisedNotes revisedNotes={dummyCurrentNotes} />}
      </div>
      {/* <div className="fixed bottom-10 right-10 flex flex-col gap-4">
        <Button className="rounded-full w-28 h-12 shadow-lg" size="lg">
          <Pencil className="mr-2" size={20} /> 생성
        </Button>
        <Button
          variant="secondary"
          className="rounded-full w-12 h-12 shadow-lg"
          size="icon"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        >
          <ArrowUp />
        </Button>
      </div> */}
    </>
  )
}

import { CircleUserRound, Search } from 'lucide-react'

import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import type { Note } from '@/types/note'

import { CurrentNotes } from './components/CurrentNotes'
import { MyNotes } from './components/MyNotes'

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
      <div className="flex flex-col p-2 mx-auto">
        <CurrentNotes currentNotes={dummyCurrentNotes} />

        <MyNotes myNotes={dummyCurrentNotes} />
      </div>
    </>
  )
}

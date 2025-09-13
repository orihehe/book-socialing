import { ArrowUp, Pencil } from 'lucide-react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { Button } from '@/components/ui/button'
import type { Note } from '@/types/note'

import { Notes as CurrentNotes } from './components/CurrentNotes/Notes'
import { Summary as CurrentSummary } from './components/CurrentNotes/Summary'
import { MyNotes } from './components/MyNotes'
import { MainLayout } from '../shared/MainLayout'
import { Notes as RevisedNotes } from './components/RevisedNotes/Notes'
import { Summary as RevisedSummary } from './components/RevisedNotes/Summary'
import { SuggestedNotes } from './components/shared/SuggestedNotes'

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
  const navigate = useNavigate()

  return (
    <>
      <MainLayout activeTab={activeTab} onTabChange={setActiveTab}>
        <div className="flex flex-col p-2 mx-auto">
          {activeTab === 'all' && (
            <>
              <CurrentSummary
                currentNotes={dummyCurrentNotes}
                moveToAll={() => setActiveTab('open')}
              />
              <MyNotes myNotes={dummyCurrentNotes} />
              <SuggestedNotes />
              <RevisedSummary
                revisedNotes={dummyCurrentNotes}
                moveToAll={() => setActiveTab('closed')}
              />
            </>
          )}
          {activeTab === 'open' && <CurrentNotes />}
          {activeTab === 'closed' && <RevisedNotes />}
        </div>
      </MainLayout>
      <div className="fixed bottom-10 right-2 flex flex-col gap-1">
        <Button
          className="rounded-full w-12 h-12 shadow-lg opacity-50"
          size="icon"
          onClick={() => navigate('/note/create')}
        >
          <Pencil />
        </Button>
        <Button
          variant="secondary"
          className="rounded-full w-12 h-12 shadow-lg opacity-50"
          size="icon"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        >
          <ArrowUp />
        </Button>
      </div>
    </>
  )
}

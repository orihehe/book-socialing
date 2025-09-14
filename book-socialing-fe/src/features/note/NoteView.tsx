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
    id: 1,
    clubName: 'saisai',
    bookName: '스프링 부트의 정석',
    bookImageUrl: 'note\\1\\0d1d9143-b7db-4461-abc2-ef36db902917.png',
    startAt: '2025-08-15T10:00:00',
    endAt: '2025-09-29T12:00:00',
  },
  {
    id: 2,
    clubName: 'saisai',
    bookName: '스프링 부트의 정석',
    bookImageUrl: 'note\\1\\0d1d9143-b7db-4461-abc2-ef36db902917.png',
    startAt: '2025-08-15T10:00:00',
    endAt: '2025-09-29T12:00:00',
  },
  {
    id: 3,
    clubName: 'saisai',
    bookName: '스프링 부트의 정석',
    bookImageUrl: 'note\\1\\0d1d9143-b7db-4461-abc2-ef36db902917.png',
    startAt: '2025-08-15T10:00:00',
    endAt: '2025-09-29T12:00:00',
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
              <CurrentSummary moveToAll={() => setActiveTab('open')} />
              <MyNotes />
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

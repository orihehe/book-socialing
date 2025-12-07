import { ArrowUp, Plus } from 'lucide-react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { Button } from '@/components/ui/button'

import { Notes as CurrentNotes } from './components/CurrentNotes/Notes'
import { Summary as CurrentSummary } from './components/CurrentNotes/Summary'
import { MyNotes } from './components/MyNotes'
import { MainLayout } from '../shared/MainLayout'
import { Notes as RevisedNotes } from './components/RevisedNotes/Notes'
import { Summary as RevisedSummary } from './components/RevisedNotes/Summary'
import { SuggestedNotes } from './components/shared/SuggestedNotes'

export default function NoteView() {
  // 0: 전체, 1: 열린노트, 2: 닫힌 노트
  const [activeChildIndex, setActiveChildIndex] = useState(0)
  const navigate = useNavigate()

  return (
    <>
      <MainLayout
        activeItemIndex={1}
        activeChildIndex={activeChildIndex}
        onChildTabChange={setActiveChildIndex}
      >
        <div className="flex flex-col p-2 mx-auto">
          {activeChildIndex === 0 && (
            <>
              <CurrentSummary moveToAll={() => setActiveChildIndex(1)} />
              <MyNotes />
              <SuggestedNotes />
              <RevisedSummary moveToAll={() => setActiveChildIndex(2)} />
            </>
          )}
          {activeChildIndex === 1 && <CurrentNotes />}
          {activeChildIndex === 2 && <RevisedNotes />}
        </div>
      </MainLayout>
      <div className="fixed bottom-13 right-2 flex flex-col gap-1">
        <Button
          className="rounded-full w-13 h-13 shadow-lg opacity-80"
          size="icon"
          onClick={() => navigate('/note/create')}
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
    </>
  )
}

import { ChevronRight } from 'lucide-react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import type { Note } from '@/types/note'

import { RevisedNoteCard } from './RevisedNoteCard'

interface RevisedNotesProps {
  revisedNotes: Note[]
  moveToAll: () => void
}

export function Summary({ revisedNotes, moveToAll }: RevisedNotesProps) {
  return (
    <>
      <BaseCard title={`닫힌 노트 (${revisedNotes.length})`}>
        <CardContent>
          <div className="grid grid-cols-3 gap-4">
            {revisedNotes.map(note => (
              <RevisedNoteCard key={note.id} note={note} />
            ))}
          </div>
        </CardContent>
      </BaseCard>
      <div className="px-4 mb-8">
        <Button
          variant="ghost"
          className="w-full justify-center items-center gap-1 text-muted-foreground text-sm px-6 py-6 rounded-5 border"
          style={{
            borderColor: '#E7ECEC',
            color: '#7D7D7D',
          }}
          onClick={moveToAll}
        >
          닫힌 노트 전체보기 <ChevronRight />
        </Button>
      </div>
    </>
  )
}

import { BaseCard } from '@/components/common/BaseCard'
import { CardContent } from '@/components/ui/card'
import type { Note } from '@/types/note'

import { RevisedNoteCard } from './RevisedNoteCard'

interface RevisedNotesProps {
  revisedNotes: Note[]
}

export function RevisedNotes({ revisedNotes }: RevisedNotesProps) {
  return (
    <BaseCard title={`퇴고한 노트 (${revisedNotes.length})`}>
      <CardContent>
        <div className="grid grid-cols-3 gap-4">
          {revisedNotes.map(note => (
            <RevisedNoteCard key={note.id} note={note} />
          ))}
        </div>
      </CardContent>
    </BaseCard>
  )
}

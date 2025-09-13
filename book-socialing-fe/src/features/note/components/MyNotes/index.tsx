import type { Note } from '@/types/note'

import { CreatedNote, AppliedNote } from './MyNoteCard'
import { ToggleNotes } from './ToggleNotes'

interface MyNotesProps {
  myNotes: Note[]
}

export function MyNotes({ myNotes }: MyNotesProps) {
  return (
    <>
      <ToggleNotes title="생성한 노트" myNotes={myNotes} NoteComponent={CreatedNote} />
      <ToggleNotes title="신청한 노트" myNotes={myNotes} NoteComponent={AppliedNote} />
    </>
  )
}

import { NoteGrid } from '@/components/common/NoteGird'
import { Note } from '@/types/note'

export function Notes() {
  const notes: Note[] = []

  return (
    <>
      <NoteGrid notes={notes} />
    </>
  )
}

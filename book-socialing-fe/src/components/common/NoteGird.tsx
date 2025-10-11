import { ArrowDownUp } from 'lucide-react' // 새로고침 아이콘
import { Link } from 'react-router-dom'

import type { Note } from '@/types/note'
import { getImageUrl } from '@/util'

interface Props {
  notes: Note[]
  totalCount: number
  children?: React.ReactNode
}

export function NoteGrid({ notes, children }: Props) {
  return (
    <div className="p2">
      <div className="flex items-center justify-between my-4 mx-4">
        <div className="flex items-center">
          <h4 className="text-lg font-bold">{notes.length ?? 0}권</h4>
          <ArrowDownUp className="h-5 w-5 text-gray-500 cursor-pointer mx-2" />
        </div>
        {children}
      </div>
      <div className="grid grid-cols-3 gap-3 p-4">
        {notes.map((note, index) => (
          <Link to={`/note/${note.id}`} key={index} className="bg-gray-200 rounded-lg h-50 w-full">
            <img
              src={getImageUrl(note.bookImageUrl ?? '')}
              alt={note.bookName}
              className="w-full h-full rounded-md object-cover border-none"
            />
          </Link>
        ))}
      </div>
    </div>
  )
}

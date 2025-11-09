import { Link } from 'react-router-dom'

import { Card } from '@/components/ui/card'
import type { Note } from '@/types/note'
import { getImageUrl } from '@/util'

type RevisedNoteCardProps = {
  note: Note
}

export function RevisedNoteCard({ note }: RevisedNoteCardProps) {
  const { id, bookName, bookAuthor, bookImageUrl } = note
  return (
    <Link to={`/note/${id}`}>
      <Card className="border-none shadow-none cursor-pointer hover:opacity-80 transition">
        <div className="relative">
          <div className="aspect-[3/4] bg-gray-200 rounded-md">
            {bookImageUrl && (
              <img
                src={getImageUrl(bookImageUrl)}
                alt={bookName}
                className="w-full h-full object-cover rounded-md"
              />
            )}
          </div>
        </div>
        <div className="mt-1">
          <div className="text-xs font-semibold truncate">{bookName}</div>
          <div className="text-[10px] text-gray-500 truncate">{bookAuthor}</div>
        </div>
      </Card>
    </Link>
  )
}

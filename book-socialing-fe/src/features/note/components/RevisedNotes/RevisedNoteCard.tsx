import { Download } from 'lucide-react'

import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import type { Note } from '@/types/note'

type RevisedNoteCardProps = {
  note: Note
}

export function RevisedNoteCard({ note }: RevisedNoteCardProps) {
  const { title, author, imageUrl } = note
  return (
    <Card className="border-none shadow-none">
      <div className="relative">
        <div className="aspect-[3/4] bg-gray-200 rounded-md">
          {imageUrl && (
            <img src={imageUrl} alt={title} className="w-full h-full object-cover rounded-md" />
          )}
        </div>
        <Button
          size="icon"
          className="absolute top-1.5 right-1.5 h-6 w-6 bg-black/30 text-white rounded-full"
        >
          <Download size={12} />
        </Button>
      </div>
      <div className="mt-1">
        <div className="text-xs font-semibold truncate">{title}</div>
        <div className="text-[10px] text-gray-500 truncate">{author}</div>
      </div>
    </Card>
  )
}

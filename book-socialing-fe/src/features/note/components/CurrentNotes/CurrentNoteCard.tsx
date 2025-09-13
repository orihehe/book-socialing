import dayjs from 'dayjs'
import { useNavigate } from 'react-router-dom'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import type { Note } from '@/types/note'

type Props = Omit<Note, 'startDateTime'>

export function CurrentNoteCard({ imageUrl, title, author, description, endDateTime, id }: Props) {
  const dDay = dayjs(endDateTime).diff(dayjs().startOf('day'), 'day')
  const navigate = useNavigate()
  return (
    <>
      <div className="flex gap-4 mt-5">
        <img src={imageUrl} alt={title} className="w-30 h-54 object-cover rounded-md border" />
        <div className="flex flex-col justify-between flex-1">
          <div>
            <div className="font-bold text-lg">{title}</div>
            <div className="text-sm text-muted-foreground">{author}</div>
            <div className="text-xs text-gray-400 mt-1">{description}</div>
          </div>
        </div>
      </div>

      <Button className="relative w-full rounded-1 bg-main text-white mt-5">
        <Badge
          variant="secondary"
          className="absolute left-2 top-1/2 -translate-y-1/2 w-6 h-6 p-0 text-[10px] bg-white/20 text-white rounded-[6px] flex items-center justify-center"
        >
          D-{dDay}
        </Badge>
        <span className="ml-6" onClick={() => navigate(`/note/${id}`)}>
          노트하러가기
        </span>
      </Button>
    </>
  )
}

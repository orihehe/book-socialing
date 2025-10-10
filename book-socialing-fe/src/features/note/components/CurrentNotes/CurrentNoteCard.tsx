import dayjs from 'dayjs'
import { useNavigate } from 'react-router-dom'

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import type { Note } from '@/types/note'
import { getImageUrl } from '@/util'

type Props = Omit<Note, 'startAt'>

export function CurrentNoteCard({
  bookImageUrl,
  bookName,
  bookAuthor,
  description,
  endAt,
  participants = [],
  id,
}: Props) {
  const dDay = dayjs(endAt).diff(dayjs().startOf('day'), 'day')
  const navigate = useNavigate()

  const visible = participants.slice(0, 3)
  const remaining = participants.length - visible.length
  return (
    <>
      <div className="flex gap-4 mt-5">
        <img
          src={getImageUrl(bookImageUrl)}
          alt={bookName}
          className="w-30 h-54 object-cover rounded-md border"
        />
        <div className="flex flex-col justify-between flex-1">
          <div>
            <div className="font-bold text-lg">{bookName}</div>
            <div className="text-sm text-muted-foreground">{bookAuthor}</div>
            <div className="text-xs text-gray-400 mt-1">{description}</div>
          </div>
          <div className="flex items-center">
            <div className="flex -space-x-2">
              {visible.map(p => (
                <Avatar key={p.participantId} className="w-6 h-6 border border-white shadow-sm">
                  {/* 나중에 user image 넣을 수 있음 */}
                  <AvatarImage src={/* getUserImage(p.userId) */ undefined} />
                  <AvatarFallback className="text-xs bg-red-400 text-white">
                    {p.userId.toString().slice(-2)} {/* fallback: userId 끝자리 */}
                  </AvatarFallback>
                </Avatar>
              ))}
            </div>
            {remaining > 0 && (
              <span className="ml-2 text-sm text-muted-foreground">+{remaining}</span>
            )}
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

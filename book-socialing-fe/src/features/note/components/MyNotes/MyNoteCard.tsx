import dayjs from 'dayjs'

import { BaseButton } from '@/components/common/BaseButton'
import type { Note } from '@/types/note'

type MyNoteCardProps = Omit<Note, 'id'>

export function MyNoteCard({ imageUrl, title, startDateTime }: MyNoteCardProps) {
  return (
    <div className="flex items-center gap-4 py-2">
      <img src={imageUrl} alt={title} className="w-14 h-14 rounded-md object-cover border" />
      <div className="flex-1">
        <div className="text-xs text-gray-400">{dayjs(startDateTime).format('YYYY.MM.DD')}</div>
        <div className="font-semibold">{title}</div>
      </div>
      <div className="flex gap-2">
        <BaseButton size="sm">수정</BaseButton>
        <BaseButton size="sm">회원관리</BaseButton>
      </div>
    </div>
  )
}

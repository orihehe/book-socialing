import dayjs from 'dayjs'

import type { Note } from '@/types/note'
import { getImageUrl } from '@/util'

interface Props extends Omit<Note, 'id'> {
  /** button components */
  children: React.ReactNode
}

export function DefaultNote({ bookImageUrl, endAt, bookName, children }: Props) {
  return (
    <div className="flex items-center gap-4 py-2 flex-wrap">
      <img
        src={getImageUrl(bookImageUrl ?? '')}
        alt={bookName}
        className="w-14 h-14 rounded-md object-cover border-none"
      />
      <div className="flex-1 min-w-0">
        <div className="text-xs text-gray-400">{dayjs(endAt).format('YYYY.MM.DD')}</div>
        <div className="font-semibold truncate">{bookName}</div>
      </div>
      <div className="flex flex-wrap gap-2 md:flex-nowrap">{children}</div>
    </div>
  )
}

import dayjs from 'dayjs'
import { Link } from 'react-router-dom'

import { getImageUrl } from '@/util'

interface NoteListItemProps {
  note: {
    id: number
    clubName?: string
    bookName: string
    bookImageUrl: string
    startAt: string
    endAt: string
    isJoined: boolean
    isHost: boolean
  }
}

export function NoteListItem({ note }: NoteListItemProps) {
  const calculateDDay = (endAt: string) => {
    const diff = dayjs(endAt).diff(dayjs().startOf('day'), 'day')
    return diff > 0 ? `D-${diff}` : diff === 0 ? 'D-Day' : `D+${Math.abs(diff)}`
  }

  const isExpired = dayjs(note.endAt).diff(dayjs().startOf('day'), 'day') < 0

  return (
    <div className="flex items-center space-x-3 py-3 border-b border-gray-100 last:border-b-0">
      <Link to={`/note/${note.id}`} className="flex items-center space-x-3 flex-1">
        <img
          className="w-16 h-16 bg-gray-200 rounded-lg flex-shrink-0 object-cover"
          src={getImageUrl(note.bookImageUrl)}
          alt={note.bookName}
        />
        <div className="flex-1 min-w-0">
          <div className="text-xs text-gray-500 mb-1">{calculateDDay(note.endAt)}</div>
          <h3 className="font-semibold text-gray-900 text-sm">{note.bookName}</h3>
        </div>
      </Link>
      {!isExpired &&
        (note.isHost ? (
          <Link
            to={`/note/${note.id}/guests`}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            관리
          </Link>
        ) : note.isJoined ? (
          <button
            onClick={e => {
              e.preventDefault()
              // TODO: 노트 신청 취소 기능 구현
            }}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            신청취소
          </button>
        ) : (
          <button
            onClick={e => {
              e.preventDefault()
              // TODO: 노트 신청 기능 구현
            }}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            신청하기
          </button>
        ))}
    </div>
  )
}

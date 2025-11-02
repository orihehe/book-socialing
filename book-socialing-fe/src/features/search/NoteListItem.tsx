import { useMutation } from '@tanstack/react-query'
import dayjs from 'dayjs'
import { Link } from 'react-router-dom'
import { toast } from 'sonner'

import { apiFetch } from '@/lib/api'
import { NoteSearchResult } from '@/types/note'
import { getImageUrl } from '@/util'

interface NoteListItemProps {
  note: NoteSearchResult
}

export function NoteListItem({ note }: NoteListItemProps) {
  const calculateDDay = (endAt: string) => {
    const diff = dayjs(endAt).diff(dayjs().startOf('day'), 'day')
    return diff > 0 ? `D-${diff}` : diff === 0 ? 'D-Day' : `D+${Math.abs(diff)}`
  }

  const isExpired = dayjs(note.endAt).diff(dayjs().startOf('day'), 'day') < 0

  const joinNoteMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/note/${note.id}/join/request`, {
        method: 'POST',
      })
    },
    onSuccess: () => {
      toast.success('노트 신청이 완료되었습니다.')
    },
    onError: () => {
      toast.error('노트 신청에 실패했습니다.')
    },
  })

  const cancelJoinNoteMutation = useMutation({
    mutationFn: async () => {
      await apiFetch(`/v1/note/${note.id}/join/cancel`, {
        method: 'PATCH',
      })
    },
    onSuccess: () => {
      toast.success('노트 신청이 취소되었습니다.')
    },
    onError: () => {
      toast.error('노트 신청 취소에 실패했습니다.')
    },
  })

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
        (note.role === 'HOST' ? (
          <Link
            to={`/note/${note.id}/guest`}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            관리
          </Link>
        ) : note.status === 'PENDING_APPROVAL' ? (
          <button
            onClick={e => {
              e.preventDefault()
              cancelJoinNoteMutation.mutate()
            }}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            신청취소
          </button>
        ) : (
          <button
            onClick={e => {
              e.preventDefault()
              joinNoteMutation.mutate()
            }}
            className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
          >
            신청하기
          </button>
        ))}
    </div>
  )
}

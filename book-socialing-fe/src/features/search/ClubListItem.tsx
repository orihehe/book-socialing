import { Link } from 'react-router-dom'

import { getImageUrl } from '@/util'

interface ClubListItemProps {
  club: {
    id: number
    clubName: string
    clubImageUrls: string[]
    description: string
    memberCount: number
    isJoined: boolean
    isHost: boolean
  }
}

export function ClubListItem({ club }: ClubListItemProps) {
  return (
    <div className="flex items-center space-x-3 py-3 border-b border-gray-100 last:border-b-0">
      <Link to={`/club/${club.id}`} className="flex items-center space-x-3 flex-1">
        <img
          className="w-16 h-16 bg-gray-200 rounded-lg flex-shrink-0 object-cover"
          src={getImageUrl(club.clubImageUrls?.[0])}
          alt={club.clubName}
        />
        <div className="flex-1 min-w-0">
          <h3 className="font-semibold text-gray-900 text-sm mb-1">{club.clubName}</h3>
          <p className="text-xs text-gray-500 line-clamp-1">
            {club.description || '소개글이 없습니다'}
          </p>
        </div>
      </Link>
      {club.isHost ? (
        <Link
          to={`/club/${club.id}/members`}
          className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
        >
          관리
        </Link>
      ) : club.isJoined ? (
        <button
          onClick={e => {
            e.preventDefault()
            // TODO: 클럽 신청 취소 기능 구현
          }}
          className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
        >
          신청취소
        </button>
      ) : (
        <button
          onClick={e => {
            e.preventDefault()
            // TODO: 클럽 신청 기능 구현
          }}
          className="px-4 py-1.5 text-xs bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex-shrink-0"
        >
          신청하기
        </button>
      )}
    </div>
  )
}


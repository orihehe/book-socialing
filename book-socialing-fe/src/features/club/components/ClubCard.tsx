import { Link } from 'react-router-dom'

import type { Club } from '@/types/club'
import { getImageUrl } from '@/util'

interface ClubCardProps {
  club: Club
  showActions?: boolean
}

export function ClubCard({ club, showActions }: ClubCardProps) {
  return (
    <Link
      to={`/club/${club.id}`}
      className="w-full flex items-start justify-start text-left space-x-3 py-3 px-1 hover:cursor-pointer"
    >
      {/* Club Image */}
      <img
        className="w-16 h-16 bg-gray-200 rounded-lg flex-shrink-0"
        src={getImageUrl(club.clubImageUrls?.[0])}
      />

      {/* Club Info */}
      <div className="flex-1 min-w-0">
        <h3 className="font-semibold text-gray-900 text-sm mb-1 truncate">{club.clubName}</h3>

        {club.description && (
          <p className="text-gray-500 text-xs leading-relaxed mb-2 line-clamp-2">
            {club.description}
          </p>
        )}

        <p className="text-gray-400 text-xs">멤버 {club.memberCount}명</p>
      </div>

      {/* Actions */}
      {showActions && (
        <div className="flex space-x-2 flex-shrink-0">
          <Link
            to={`/club/${club.id}/edit`}
            className="px-2 py-1 text-xs text-gray-500 bg-gray-100 rounded hover:bg-gray-200"
          >
            수정
          </Link>
          <Link
            to={`/club/${club.id}/members`}
            className="px-2 py-1 text-xs text-gray-500 bg-gray-100 rounded hover:bg-gray-200"
          >
            관리
          </Link>
        </div>
      )}
    </Link>
  )
}

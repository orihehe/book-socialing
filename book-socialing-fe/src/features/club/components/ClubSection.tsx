import { RefreshCw } from 'lucide-react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import type { ClubSection as ClubSectionType } from '@/types/club'

import { ClubCard } from './ClubCard'

interface ClubSectionProps {
  section: ClubSectionType
}

export function ClubSection({ section }: ClubSectionProps) {
  if (!section.clubs.length) return null

  return (
    <BaseCard
      title={
        <div className="flex">
          {section.title}
          {section.refetch ? (
            <div className="releation top-4 right-4">
              <button
                onClick={() => section.refetch!()}
                className="p-2 hover:text-gray-700 cursor-pointer"
              >
                <RefreshCw size={16} />
              </button>
            </div>
          ) : (
            <> ({section.totalCount})</>
          )}
        </div>
      }
    >
      {/* Club Cards */}
      <div className="space-y-3">
        {section.clubs.slice(0, 2).map(club => (
          <ClubCard key={club.id} club={club} showActions={section.showActions} />
        ))}
      </div>

      {/* View All Button */}
      {section.showViewAll && section.totalCount > 2 && (
        <div className="mt-4 text-center">
          <Button variant="outline" size="sm" className="w-full">
            전체보기
          </Button>
        </div>
      )}
    </BaseCard>
  )
}

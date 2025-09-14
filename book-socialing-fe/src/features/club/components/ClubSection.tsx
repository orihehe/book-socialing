import { RefreshCw } from 'lucide-react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import type { ClubSection as ClubSectionType } from '@/types/club'

import { ClubCard } from './ClubCard'

interface ClubSectionProps {
  section: ClubSectionType
}

export function ClubSection({ section }: ClubSectionProps) {
  return (
    <BaseCard title={`${section.title} (${section.count})`}>
      {/* Section Header with Refresh Button */}
      {section.title === '추천클럽' && (
        <div className="releation top-4 right-4">
          <button className="p-2 text-gray-500 hover:text-gray-700">
            <RefreshCw size={16} />
          </button>
        </div>
      )}

      {/* Club Cards */}
      <div className="space-y-3">
        {section.clubs.slice(0, 2).map(club => (
          <ClubCard key={club.id} club={club} showActions={section.showActions} />
        ))}
      </div>

      {/* View All Button */}
      {section.showViewAll && section.count > 2 && (
        <div className="mt-4 text-center">
          <Button variant="outline" size="sm" className="w-full">
            전체보기
          </Button>
        </div>
      )}
    </BaseCard>
  )
}

import { useQuery } from '@tanstack/react-query'

import { BaseButton } from '@/components/common/BaseButton'
import { BaseCard } from '@/components/common/BaseCard'
import { CardContent } from '@/components/ui/card'
import { apiFetch } from '@/lib/api'
import type { ClubNotesPageResponse } from '@/types/note'

import { DefaultNote } from './DefaultNote'

export function SuggestedNotes() {
  const { data: suggestedNotes } = useQuery<ClubNotesPageResponse>({
    queryKey: ['suggestedNotes'],
    queryFn: async () => {
      const res = await apiFetch('/v1/note/recommend')
      if (!res.ok) throw new Error('Failed to fetch suggested notes')
      return res.json()
    },
  })

  if (!suggestedNotes?.totalCount) return null

  return (
    <BaseCard title={`추천 노트 (${suggestedNotes.totalCount})`}>
      <CardContent>
        {suggestedNotes.groups
          .flatMap(group => group.notes)
          .map(note => (
            <DefaultNote key={note.id} {...note}>
              <BaseButton className="flex-shrink-0">신청</BaseButton>
            </DefaultNote>
          ))}
      </CardContent>
    </BaseCard>
  )
}

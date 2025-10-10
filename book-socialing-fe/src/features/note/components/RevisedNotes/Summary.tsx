import { useQuery } from '@tanstack/react-query'
import { ChevronRight } from 'lucide-react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import type { ClubNotesPageResponse } from '@/types/note'

import { RevisedNoteCard } from './RevisedNoteCard'

interface RevisedNotesProps {
  moveToAll: () => void
}

export function Summary({ moveToAll }: RevisedNotesProps) {
  const { data } = useQuery({
    queryKey: ['revisedNoteSummary'],
    queryFn: async (): Promise<ClubNotesPageResponse> => {
      const response = await fetch('/api/note/v1/revised', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to fetch open notes')
      }

      return response.json()
    },
  })

  return (
    <>
      <BaseCard title={`닫힌 노트 (${data?.totalCount ?? 0})`}>
        <CardContent>
          <div className="grid grid-cols-3 gap-4">
            {data?.groups
              .flatMap(({ notes }) => notes)
              .map(note => <RevisedNoteCard key={note.id} note={note} />)}
          </div>
        </CardContent>
      </BaseCard>
      <div className="px-4 mb-8">
        <Button
          variant="ghost"
          className="w-full justify-center items-center gap-1 text-muted-foreground text-sm px-6 py-6 rounded-5 border"
          style={{
            borderColor: '#E7ECEC',
            color: '#7D7D7D',
          }}
          onClick={moveToAll}
        >
          닫힌 노트 전체보기 <ChevronRight />
        </Button>
      </div>
    </>
  )
}

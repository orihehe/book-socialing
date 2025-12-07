import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { RefreshCw } from 'lucide-react'
import { toast } from 'sonner'

import { BaseButton } from '@/components/common/BaseButton'
import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import { apiFetch } from '@/lib/api'
import type { ClubNotesPageResponse } from '@/types/note'

import { DefaultNote } from './DefaultNote'

export function SuggestedNotes() {
  const queryClient = useQueryClient()

  const {
    data: suggestedNotes,
    refetch,
    isRefetching,
  } = useQuery<ClubNotesPageResponse>({
    queryKey: ['suggestedNotes'],
    queryFn: async () => {
      const res = await apiFetch('/v1/note/recommend')
      return res.json()
    },
  })

  const handleRefresh = () => {
    refetch()
  }

  const joinNoteMutation = useMutation({
    mutationFn: async (noteId: number) => {
      await apiFetch(`/v1/note/${noteId}/join/request`, {
        method: 'POST',
      })
      return true
    },
    onSuccess: () => {
      toast.success('노트 신청이 완료되었습니다.')
      // 추천 노트 목록 갱신
      queryClient.invalidateQueries({ queryKey: ['suggestedNotes'] })
    },
    onError: () => {
      toast.error('노트 신청에 실패했습니다.')
    },
  })

  if (!suggestedNotes?.totalCount) return null

  return (
    <BaseCard
      title={
        <div className="flex items-center justify-between">
          <span>추천 노트</span>
          <Button
            variant="ghost"
            size="icon"
            onClick={handleRefresh}
            disabled={isRefetching}
            className="h-8 w-8"
          >
            <RefreshCw className={`h-4 w-4 ${isRefetching ? 'animate-spin' : ''}`} />
          </Button>
        </div>
      }
    >
      <CardContent>
        {suggestedNotes.groups
          .flatMap(group => group.notes)
          .map(note => (
            <DefaultNote key={note.id} {...note}>
              <BaseButton
                className="flex-shrink-0"
                onClick={() => joinNoteMutation.mutate(note.id)}
              >
                신청
              </BaseButton>
            </DefaultNote>
          ))}
      </CardContent>
    </BaseCard>
  )
}

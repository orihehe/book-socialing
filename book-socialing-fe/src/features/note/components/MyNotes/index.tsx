import { useQueries } from '@tanstack/react-query'

import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { apiFetch } from '@/lib/api'
import { ClubNotesPageResponse } from '@/types/note'

import { AppliedNote, CreatedNote } from './MyNoteCard'
import { ToggleNotes } from './ToggleNotes'

export function MyNotes() {
  const results = useQueries({
    queries: [
      {
        queryKey: ['createdNotes'],
        queryFn: async (): Promise<ClubNotesPageResponse> => {
          const response = await apiFetch('/v1/note/created', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          })
          return response.json()
        },
      },
      // {
      //   queryKey: ['pendingNotes'],
      //   queryFn: async (): Promise<ClubNotesPageResponse> => {
      //     const response = await apiFetch('/v1/note/pending', {
      //       method: 'GET',
      //       headers: {
      //         'Content-Type': 'application/json',
      //       },
      //     })

      //     if (!response.ok) {
      //       throw new Error('Failed to fetch pending notes')
      //     }

      //     return response.json()
      //   },
      // },
    ],
  })

  const pendingResult = { data: { groups: [], totalCount: 0 }, error: false, isLoading: false }

  // const [createdResult, pendingResult] = results
  const [createdResult] = results
  const isLoading = createdResult.isLoading || pendingResult.isLoading

  if (isLoading) return <LoadingPage className="h-30" />

  return (
    <>
      <ToggleNotes title="생성한 노트" result={createdResult.data} NoteComponent={CreatedNote} />
      <ToggleNotes title="신청한 노트" result={pendingResult.data} NoteComponent={AppliedNote} />
    </>
  )
}

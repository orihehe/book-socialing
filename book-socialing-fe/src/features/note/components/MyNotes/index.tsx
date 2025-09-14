import { useQueries } from '@tanstack/react-query'

import { ClubNotesPageResponse } from '@/types/note'

import { AppliedNote, CreatedNote } from './MyNoteCard'
import { ToggleNotes } from './ToggleNotes'

export function MyNotes() {
  const results = useQueries({
    queries: [
      {
        queryKey: ['createdNotes'],
        queryFn: async (): Promise<ClubNotesPageResponse> => {
          const response = await fetch('/api/note/v1/created', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          })

          if (!response.ok) {
            throw new Error('Failed to fetch created notes')
          }

          return response.json()
        },
      },
      // {
      //   queryKey: ['pendingNotes'],
      //   queryFn: async (): Promise<ClubNotesPageResponse> => {
      //     const response = await fetch('/api/note/v1/pending', {
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
  const hasError = createdResult.error || pendingResult.error
  if (isLoading) return <div>Loading...</div>
  if (hasError) return <div>Error occurred</div>

  return (
    <>
      <ToggleNotes title="생성한 노트" result={createdResult.data} NoteComponent={CreatedNote} />
      <ToggleNotes title="신청한 노트" result={pendingResult.data} NoteComponent={AppliedNote} />
    </>
  )
}

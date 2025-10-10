import { useQuery } from '@tanstack/react-query'

import { NoteGrid } from '@/components/common/NoteGird'
import { ClubNotesPageResponse } from '@/types/note'

export function Notes() {
  const { data } = useQuery({
    queryKey: ['opendNotes'],
    queryFn: async (): Promise<ClubNotesPageResponse> => {
      const response = await fetch('/api/v1/note/open', {
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

  const notes = data?.groups.flatMap(({ notes }) => notes) ?? []
  return (
    <>
      <NoteGrid notes={notes} totalCount={data?.totalCount ?? 0} />
    </>
  )
}

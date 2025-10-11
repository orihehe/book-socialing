import { useQuery } from '@tanstack/react-query'
import dayjs from 'dayjs'
import { useState } from 'react'
import { useParams } from 'react-router-dom'

import { Label } from '@/components/ui/label'
import { apiFetch } from '@/lib/api'
import type { ClubNotesGroup, Note } from '@/types/note'
import { getImageUrl } from '@/util'

export default function NoteSelector() {
  const { id } = useParams<{ id: string }>()
  const [selectedNote, setSelectedNote] = useState<Note>()

  const noteQuery = useQuery({
    queryKey: ['club', id, 'notes'],
    queryFn: async () => {
      // TODO: club 조회로 바꾸기
      const res = await apiFetch(`/v1/note/open`)
      if (!res.ok) throw new Error('클럽 멤버 정보를 불러오지 못했습니다.')
      const result = await res.json()

      return result?.groups?.[1] as ClubNotesGroup
    },
    enabled: !!id,
  })

  return (
    <div className="mb-10">
      <div className="space-y-2 my-7">
        <Label className="text-base font-bold">클럽 내역</Label>
        <div
          className="overflow-x-auto mt-2"
          style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
        >
          <div className="flex gap-3 w-max">
            {noteQuery.data?.notes.map(note => (
              <button
                key={note.id}
                onClick={() => setSelectedNote(note)}
                className="w-15 h-15 rounded-lg bg-gray-300 shrink-0 hover:cursor-pointer"
                style={{
                  backgroundImage: `url(${getImageUrl(note.bookImageUrl)})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                }}
              />
            ))}
          </div>
        </div>
      </div>

      {selectedNote && (
        <div>
          <h2 className="text-2xl font-bold mb-2 text-center w-full">{selectedNote.bookName}</h2>
          <div className="flex gap-4 bg-white rounded-xl border-none w-100">
            {/* 책 표지 */}
            <div className="w-[140px] h-[220px] bg-gray-100 rounded-md overflow-hidden flex-shrink-0">
              {selectedNote.bookImageUrl ? (
                <img
                  src={getImageUrl(selectedNote.bookImageUrl)}
                  alt={selectedNote.bookName}
                  className="w-full h-full object-cover"
                />
              ) : (
                <div className="w-full h-full bg-gray-200" />
              )}
            </div>

            {/* 책 정보 */}
            <div className="flex flex-col gap-10">
              <div>
                <p>모임날짜</p>
                <div className="flex items-center gap-2 text-sm text-gray-600 mb-1">
                  <span>{dayjs(selectedNote.endAt).format('YYYY.MM.DD')}</span>
                </div>
              </div>

              {/* 책 설명 */}
              <div>
                <p>책정보</p>
                <div className="text-sm text-gray-700 leading-snug line-clamp-5">
                  {selectedNote.description}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

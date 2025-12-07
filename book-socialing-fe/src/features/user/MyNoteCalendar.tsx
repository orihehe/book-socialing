import { useQuery } from '@tanstack/react-query'
import dayjs from 'dayjs'
import { ChevronLeft, ChevronRight } from 'lucide-react'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { apiFetch } from '@/lib/api'
import { Note } from '@/types/note'
import { getImageUrl } from '@/util'

export default function NoteScheduleCalendar() {
  const navigate = useNavigate()
  const [month, setMonth] = React.useState(dayjs().startOf('month'))
  const [open, setOpen] = useState(false)
  const [selectedNotes, setSelectedNotes] = useState<Note[]>([])
  const [dateType] = useState<'START' | 'END'>('END')

  const startDate = month.format('YYYY-MM-DD')
  const endDate = month.endOf('month').format('YYYY-MM-DD')

  const { data: notesByDate } = useQuery({
    queryKey: ['myNotes', dateType, startDate, endDate],
    queryFn: async () => {
      const params = new URLSearchParams({
        dateType,
        startDate,
        endDate,
      })

      const res = await apiFetch(`/v1/note/participated?${params.toString()}`)

      const map: Record<string, Note[]> = {}
      const dateNotes: { date: string; notes: Note[] }[] = await res.json()

      dateNotes.forEach(({ date, notes }) => {
        map[date] = notes
      })

      return map
    },
  })

  const daysInMonth = month.daysInMonth()
  const firstDayOfWeek = month.startOf('month').day()
  const blanks = Array.from({ length: firstDayOfWeek })

  const handlePrevMonth = () => setMonth(prev => prev.subtract(1, 'month'))
  const handleNextMonth = () => setMonth(prev => prev.add(1, 'month'))

  return (
    <div className="flex flex-col p-4 items-center bg-white min-h-screen">
      {/* 헤더 */}
      <header className="w-full flex items-center justify-between mb-4">
        <h2 className="text-lg font-bold text-gray-900">노트 일정 관리</h2>
      </header>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>노트 선택</DialogTitle>
          </DialogHeader>

          <div className="flex flex-col gap-2 mt-2">
            {selectedNotes.map(note => (
              <button
                key={note.id}
                className="flex items-center gap-3 p-2 rounded-md border hover:bg-gray-100"
                onClick={() => {
                  setOpen(false)
                  navigate(`/note/${note.id}`)
                }}
              >
                <img src={note.bookImageUrl} className="w-10 h-14 object-cover rounded-sm" />
                <div className="text-sm font-medium truncate">{note.bookName || '제목 없음'}</div>
              </button>
            ))}
          </div>
        </DialogContent>
      </Dialog>

      {/* 월 네비게이션 */}
      <div className="flex items-center justify-between w-full my-4">
        <button onClick={handlePrevMonth}>
          <ChevronLeft className="w-5 h-5" />
        </button>
        <span className="text-lg font-semibold">{month.format('YYYY.MM')}</span>
        <button onClick={handleNextMonth}>
          <ChevronRight className="w-5 h-5" />
        </button>
      </div>

      {/* 요일 헤더 */}
      <div className="grid grid-cols-7 text-xs text-center text-gray-500 w-full mb-2">
        {['일', '월', '화', '수', '목', '금', '토'].map(d => (
          <div key={d}>{d}</div>
        ))}
      </div>

      {/* 날짜 셀 */}
      <div className="grid grid-cols-7 gap-3 w-full">
        {/* 앞쪽 빈칸 */}
        {blanks.map((_, idx) => (
          <div key={`blank-${idx}`} className="h-[72px]" />
        ))}

        {/* 날짜 */}
        {Array.from({ length: daysInMonth }, (_, i) => i + 1).map(day => {
          const currentDate = month.date(day)
          const dateKey = currentDate.format('YYYY-MM-DD')
          const notes = notesByDate?.[dateKey] ?? []

          return (
            <button
              key={day}
              className={`flex flex-col items-center gap-1${notes.length > 0 ? ' cursor-pointer' : ''}`}
              disabled={!dateKey}
              onClick={() => {
                if (notes.length === 1) {
                  navigate(`/note/${notes[0].id}`)
                } else if (notes.length > 1) {
                  setSelectedNotes(notes)
                  setOpen(true)
                }
              }}
            >
              <div className="relative w-12 h-18 rounded-md overflow-hidden">
                {notes.length > 0 ? (
                  <div className="relative w-full h-full">
                    {notes.slice(0, 3).map((n, i) => (
                      <img
                        key={n.id}
                        src={getImageUrl(n.bookImageUrl)}
                        alt=""
                        className="absolute w-full h-full object-cover rounded-md"
                        style={{
                          transform: `translate(${i * 3}px, ${i * 3}px)`,
                          zIndex: i,
                        }}
                      />
                    ))}

                    {notes.length > 3 && (
                      <div className="absolute bottom-0 right-0 bg-black/50 text-white text-[9px] px-1 rounded">
                        +{notes.length - 3}
                      </div>
                    )}
                  </div>
                ) : (
                  <div className="absolute inset-0 bg-gray-200" />
                )}
              </div>
              <span className="text-[11px] font-semibold text-gray-500 leading-none">{day}</span>
            </button>
          )
        })}
      </div>
    </div>
  )
}

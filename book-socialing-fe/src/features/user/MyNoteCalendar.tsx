import dayjs from 'dayjs'
import { ChevronLeft, ChevronRight } from 'lucide-react'
import * as React from 'react'
import { useNavigate } from 'react-router-dom'

import { Note } from '@/types/note'

const mockNotes: Partial<Note>[] = [
  { id: 1, endAt: '2025-07-12', bookImageUrl: '/covers/book1.jpg' },
  { id: 2, endAt: '2025-07-25', bookImageUrl: '/covers/book2.jpg' },
]

export default function NoteScheduleCalendar() {
  const navigate = useNavigate()
  const [month, setMonth] = React.useState(dayjs('2025-07-01'))

  const notesByDate = React.useMemo(() => {
    const map: Record<string, Partial<Note>> = {}
    mockNotes.forEach(note => {
      map[dayjs(note.endAt).format('YYYY-MM-DD')] = note
    })
    return map
  }, [])

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
          const { id, bookImageUrl } = notesByDate[dateKey] ?? {}

          return (
            <button
              key={day}
              className={`flex flex-col items-center gap-1${id ? ' cursor-pointer' : ''}`}
              disabled={!id}
              onClick={() => {
                if (id) navigate(`/note/${id}`)
              }}
            >
              <div className="relative w-12 h-18 rounded-md overflow-hidden">
                {bookImageUrl ? (
                  <img
                    src={bookImageUrl}
                    alt=""
                    className="absolute inset-0 w-full h-full object-cover"
                  />
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

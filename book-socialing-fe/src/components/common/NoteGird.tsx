import { ArrowDownUp } from 'lucide-react' // 새로고침 아이콘

interface Props {
  notes: object[]
  children?: React.ReactNode
}

export function NoteGrid({ notes, children }: Props) {
  return (
    <div className="p2">
      <div className="flex items-center justify-between my-4 mx-4">
        <div className="flex items-center">
          <h4 className="text-lg font-bold">{notes.length ?? 0}권</h4>
          <ArrowDownUp className="h-5 w-5 text-gray-500 cursor-pointer mx-2" />
        </div>
        {children}
      </div>
      <div className="grid grid-cols-3 gap-3 p-4">
        {notes.map((_, index) => (
          <div key={index} className="bg-gray-200 rounded-lg h-32 w-full">
            {/* 실제 노트 내용이 들어갈 곳 */}
          </div>
        ))}
      </div>
    </div>
  )
}

import { MessageType } from '@/types/chat'

interface MessageProps {
  name?: string
  time?: string
  text: string
  //   reactions?: Record<Reaction, number>
  edited?: boolean
  type: MessageType
}

// type Reaction = 'like' | 'heart' | 'laugh' | 'kiss' | 'wow' | 'love'

// const ReactionCode: Record<Reaction, string> = {
//   like: '👍',
//   heart: '❤️',
//   laugh: '😂',
//   kiss: '😘',
//   wow: '😲',
//   love: '😍',
export function Message({ name, text, time, type }: MessageProps) {
  return (
    <div className="flex flex-col gap-1">
      <div className="text-sm font-semibold">{name}</div>
      <div className="flex gap-2 relative w-fit max-w-[80%]">
        {/* 말풍선 */}
        <div className="bg-[#EEF2F4] text-sm px-4 py-2 rounded-xl text-black whitespace-pre-line">
          {text}
        </div>
        <div className="flex flex-col gap-0 width-auto">
          {/* 타입 태그 */}
          {type !== MessageType.GENERAL && (
            <span className="text-[11px] text-gray-500 mb-1">[{type}]</span>
          )}

          {/* 시간 */}
          <span className="text-[10px] text-gray-700">오후 {time}</span>
        </div>
      </div>
    </div>
  )
}

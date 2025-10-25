import dayjs from 'dayjs'
import { UserRound } from 'lucide-react'

import { ChatMessageResponse, MessageType } from '@/types/chat'

import { MESSAGE_TYPE_LABELS } from './const'

// type Reaction = 'like' | 'heart' | 'laugh' | 'kiss' | 'wow' | 'love'

// const ReactionCode: Record<Reaction, string> = {
//   like: '👍',
//   heart: '❤️',
//   laugh: '😂',
//   kiss: '😘',
//   wow: '😲',
//   love: '😍',

interface Props extends ChatMessageResponse {
  onUserClick: (userId: number) => void
}

export function Message({ onUserClick, content, type, sentAt, userId }: Props) {
  return (
    <div className="flex gap-2">
      <button className="w-8 h-8 rounded-full cursor-pointer" onClick={() => onUserClick(userId)}>
        <UserRound className="w-8 h-8 rounded-full" />
      </button>

      <div className="flex flex-col gap-1 flex-1 min-w-0">
        <div className="text-sm font-semibold">{userId}</div>
        <div className="flex gap-2 relative">
          {/* 말풍선 */}
          <div className="bg-[#EEF2F4] text-sm px-4 py-2 rounded-xl text-black whitespace-pre-wrap break-words max-w-[calc(100vw-120px)]">
            {content}
          </div>
          <div className="flex flex-col gap-0 flex-shrink-0">
            {/* 타입 태그 */}
            {type !== MessageType.GENERAL && (
              <div className="text-[11px] text-gray-500">
                [{MESSAGE_TYPE_LABELS[type as MessageType]}]
              </div>
            )}

            {/* 시간 */}
            <div className="text-[10px] text-gray-700">{dayjs(sentAt).format('a hh:mm')}</div>
          </div>
        </div>
      </div>
    </div>
  )
}

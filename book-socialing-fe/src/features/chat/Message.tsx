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
  onUserClick: (senderNickname: string) => void
}
export function Message({ onUserClick, senderNickname, content, type, sentAt }: Props) {
  return (
    <div className="flex gap-2">
      <button
        className="w-8 h-8 rounded-full cursor-pointer"
        onClick={() => onUserClick(senderNickname)}
      >
        <UserRound className="w-8 h-8 rounded-full" />
      </button>

      <div className="flex flex-col gap-1">
        <div className="text-sm font-semibold">{senderNickname}</div>
        <div className="flex gap-2 relative w-fit max-w-[80%]">
          {/* 말풍선 */}
          <div className="bg-[#EEF2F4] text-sm px-4 py-2 rounded-xl text-black whitespace-pre-line">
            {content}
          </div>
          <div className="flex flex-col gap-0 w-auto">
            {/* 타입 태그 */}
            {type !== MessageType.GENERAL && (
              <div className="text-[11px] text-gray-500">[{MESSAGE_TYPE_LABELS[type]}]</div>
            )}

            {/* 시간 */}
            <div className="text-[10px] text-gray-700">{dayjs(sentAt).format('a hh:mm')}</div>
          </div>
        </div>
      </div>
    </div>
  )
}

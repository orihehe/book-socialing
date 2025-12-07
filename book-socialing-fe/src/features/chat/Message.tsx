import dayjs from 'dayjs'
import { UserRound } from 'lucide-react'

import { ChatMessageResponse, MessageType } from '@/types/chat'
import { ChatUser } from '@/types/user'

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
  user?: ChatUser
  isMine?: boolean
}

export function Message({ onUserClick, isMine, content, type, sentAt, userId, user }: Props) {
  const isMineMessage = isMine === true

  return (
    <div className={`flex gap-2 ${isMineMessage ? 'justify-end text-right' : 'justify-start'}`}>
      {/* 내 메시지는 프로필을 오른쪽으로 보내야 하니까 */}
      {!isMineMessage && (
        <button
          className="w-8 h-8 rounded-full cursor-pointer overflow-hidden bg-gray-200 flex items-center justify-center"
          onClick={() => onUserClick(userId)}
        >
          {user?.profileImageUrl ? (
            <img src={user.profileImageUrl} className="w-full h-full object-cover" />
          ) : (
            <UserRound className="w-5 h-5 text-gray-600" />
          )}
        </button>
      )}

      <div className="flex flex-col gap-1 max-w-[70%]">
        {!isMineMessage && (
          <div className="text-sm font-semibold">{user?.nickname || `User ${userId}`}</div>
        )}

        <div className={`flex gap-2 ${isMineMessage ? 'flex-row-reverse' : ''}`}>
          {/* 말풍선 */}
          <div
            className={`text-sm px-4 py-2 rounded-xl whitespace-pre-wrap break-words
              ${isMineMessage ? 'bg-[#D1ECFF] text-black' : 'bg-[#EEF2F4] text-black'}`}
          >
            {content}
          </div>

          {/* 타입 + 시간 */}
          <div className={`flex flex-col gap-0 flex-shrink-0 ${isMineMessage ? 'items-end' : ''}`}>
            {type !== MessageType.GENERAL && (
              <div className="text-[11px] text-gray-500">
                [{MESSAGE_TYPE_LABELS[type as MessageType]}]
              </div>
            )}
            <div className="text-[10px] text-gray-700">
              {dayjs.utc(sentAt).local().format('HH:mm')}
            </div>
          </div>
        </div>
      </div>

      {/* 내 메시지면 프로필을 오른쪽에 */}
      {isMineMessage && (
        <button
          className="w-8 h-8 rounded-full cursor-pointer overflow-hidden bg-gray-200 flex items-center justify-center"
          onClick={() => onUserClick(userId)}
        >
          {user?.profileImageUrl ? (
            <img src={user.profileImageUrl} className="w-full h-full object-cover" />
          ) : (
            <UserRound className="w-5 h-5 text-gray-600" />
          )}
        </button>
      )}
    </div>
  )
}

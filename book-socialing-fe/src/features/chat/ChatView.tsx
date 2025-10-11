import { ChevronLeft, Search } from 'lucide-react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { ChatMessageResponse, MessageType } from '@/types/chat'
import { User } from '@/types/user'

import { ChatInput } from './ChatInput'
import { FilterType } from './const'
import { DownloadButton } from './DownloadButton'
import { Filter } from './Filter'
import { Message } from './Message'
import { UserDialog } from '../shared/components/UserDialog'

const dummyMessages: ChatMessageResponse[] = [
  {
    messageId: '1',
    senderNickname: '유저1',
    content: '9월 9일 오후 8시 서울역 카페에서 만나요\n~^^',
    type: MessageType.NOTICE,
    sentAt: '2025-09-09T19:00:00',
  },
  {
    messageId: '2',
    senderNickname: '유저2',
    content: '반전 미쳤다',
    type: MessageType.REVIEW,
    sentAt: '2025-09-09T19:06:00',
  },
  {
    messageId: '3',
    senderNickname: '유저2',
    content: '근데 내가 이 상황이었다면?',
    type: MessageType.QUESTION,
    sentAt: '2025-09-09T19:10:00',
  },
  {
    messageId: '4',
    senderNickname: '유저3',
    content: '너무 감동적 p.45',
    type: MessageType.REVIEW,
    sentAt: '2025-09-09T19:11:00',
  },
]

export default function ChatPage() {
  const [openUserDialog, setOpenUserDialog] = useState(false)
  const [selectedUser, setSelectedUser] = useState<User>()
  const [activeFilter, setActiveFilter] = useState<FilterType>()
  const navigate = useNavigate()

  function handleUserClick(senderNickname: string) {
    setOpenUserDialog(true)
    // TODO: user 조회
    setSelectedUser({ nickname: senderNickname, email: senderNickname, id: 1 })
  }

  return (
    <div className="flex flex-col h-screen bg-[#FAFAFA]">
      <header className="flex justify-between px-4 py-3">
        <button>
          <ChevronLeft onClick={() => navigate(-1)} />
        </button>
        <div className="flex gap-4">
          <div className="text-sm text-gray-500">2025.04.02</div>
          <h1 className="text-lg font-bold">빛과 실</h1>
        </div>
        <div className="flex gap-1">
          <button>
            <Search />
          </button>
          <DownloadButton />
        </div>
      </header>

      <main className="flex-1 overflow-y-auto px-4 py-2 space-y-4">
        <Filter activeFilter={activeFilter} setActiveFilter={setActiveFilter} />
        {dummyMessages.map(message => (
          <Message key={message.messageId} onUserClick={handleUserClick} {...message} />
        ))}
        <UserDialog user={selectedUser} open={openUserDialog} setOpen={setOpenUserDialog} />
      </main>

      <footer className="p-2">
        <ChatInput />
      </footer>
    </div>
  )
}

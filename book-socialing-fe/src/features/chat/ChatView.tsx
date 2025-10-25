import { ChevronLeft, Search } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import { useWebSocket } from '@/hooks/useWebSocket'
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
    messageId: 1,
    senderNickname: '유저1',
    content: '9월 9일 오후 8시 서울역 카페에서 만나요\n~^^',
    type: MessageType.NOTICE,
    sentAt: '2025-09-09T19:00:00',
  },
  {
    messageId: 2,
    senderNickname: '유저2',
    content: '반전 미쳤다',
    type: MessageType.REVIEW,
    sentAt: '2025-09-09T19:06:00',
  },
  {
    messageId: 3,
    senderNickname: '유저2',
    content: '근데 내가 이 상황이었다면?',
    type: MessageType.QUESTION,
    sentAt: '2025-09-09T19:10:00',
  },
  {
    messageId: 4,
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
  const [messages, setMessages] = useState<ChatMessageResponse[]>(dummyMessages)
  const navigate = useNavigate()
  const { id: noteId } = useParams<{ id: string }>()

  // JWT 토큰을 localStorage에서 가져오기
  const token = localStorage.getItem('accessToken') || localStorage.getItem('token') || ''

  const { isConnected, isConnecting, connect, sendMessage } = useWebSocket({
    token,
    noteId: noteId ? Number(noteId) : undefined,
    onMessage: message => {
      setMessages(prev => [...prev, message])
    },
    onConnect: () => {
      console.log('WebSocket connected')
    },
    onDisconnect: () => {
      console.log('WebSocket disconnected')
    },
    onError: error => {
      console.error('WebSocket error:', error)
    },
  })

  // 컴포넌트 마운트 시 WebSocket 연결 (한 번만)
  useEffect(() => {
    if (!token) {
      console.warn('No token found, redirecting to login')
      navigate('/signin')
      return
    }
    if (noteId && !isConnected && !isConnecting) {
      connect()
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [noteId, token, navigate])

  function handleUserClick(senderNickname: string) {
    setOpenUserDialog(true)
    // TODO: user 조회
    setSelectedUser({ nickname: senderNickname, email: senderNickname, id: 1 })
  }

  function handleSendMessage(content: string, type: MessageType) {
    sendMessage(content, type)
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
        {messages.map(message => (
          <Message key={message.messageId} onUserClick={handleUserClick} {...message} />
        ))}
        <UserDialog user={selectedUser} open={openUserDialog} setOpen={setOpenUserDialog} />
      </main>

      <footer className="p-2">
        <ChatInput onSendMessage={handleSendMessage} disabled={!isConnected} />
        {!isConnected && (
          <div className="text-center text-sm text-gray-500 px-4">
            {isConnecting ? '연결 중...' : '연결되지 않음'}
          </div>
        )}
      </footer>
    </div>
  )
}

import { useQuery } from '@tanstack/react-query'
import dayjs from 'dayjs'
import { ChevronLeft, Search } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import { useWebSocket } from '@/hooks/useWebSocket'
import { apiFetch } from '@/lib/api'
import { ChatMessageResponse, MessageType } from '@/types/chat'
import { Note } from '@/types/note'
import { User } from '@/types/user'

import { ChatInput } from './ChatInput'
import { FilterType } from './const'
import { DownloadButton } from './DownloadButton'
import { Filter } from './Filter'
import { Message } from './Message'
import { UserDialog } from '../shared/components/UserDialog'

export default function ChatPage() {
  const [openUserDialog, setOpenUserDialog] = useState(false)
  const [selectedUser, setSelectedUser] = useState<User>()
  const [activeFilter, setActiveFilter] = useState<FilterType>()
  const [messages, setMessages] = useState<ChatMessageResponse[]>([])
  const navigate = useNavigate()
  const { id: noteId } = useParams<{ id: string }>()

  // 노트 데이터 가져오기
  const { data: noteData } = useQuery({
    queryKey: ['note', noteId],
    queryFn: async (): Promise<Note> => {
      const response = await apiFetch(`/v1/note/${noteId}`)
      if (!response.ok) {
        throw new Error('Failed to fetch note data')
      }
      return response.json()
    },
    enabled: !!noteId,
  })

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

  // 이전 채팅기록 useQuery로 불러오기
  const { data: chatHistory } = useQuery({
    queryKey: ['chatHistory', noteId],
    queryFn: async (): Promise<ChatMessageResponse[]> => {
      if (!noteId) return []
      const response = await apiFetch(`/api/v1/chat/rooms/${noteId}/messages`)
      if (!response.ok) {
        throw new Error('Failed to fetch chat history')
      }
      return response.json()
    },
    enabled: !!noteId,
  })

  // WebSocket 연결 전에 이전 채팅 기록을 한 번만 로드
  useEffect(() => {
    if (chatHistory && !isConnected && messages.length === 0) {
      setMessages(chatHistory)
    }
  }, [chatHistory, isConnected, messages.length])

  // 컴포넌트 마운트 시 WebSocket 연결 (한 번만)
  useEffect(() => {
    if (!token) {
      console.warn('No token found, redirecting to login')
      navigate('/sign-in')
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
          <div className="text-sm text-gray-500">
            {noteData?.endAt ? dayjs(noteData.endAt).format('YYYY.MM.DD') : ''}
          </div>
          <h1 className="text-lg font-bold">{noteData?.bookName || '로딩중...'}</h1>
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

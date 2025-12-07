import { useQuery } from '@tanstack/react-query'
import dayjs from 'dayjs'
import { ChevronLeft, Search } from 'lucide-react'
import { useEffect, useMemo, useRef, useState } from 'react'
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
  const [userMap, setUserMap] = useState<Map<number, User>>(new Map())
  const navigate = useNavigate()
  const { id: noteId } = useParams<{ id: string }>()
  const messagesEndRef = useRef<HTMLDivElement>(null)

  // 노트 데이터 가져오기
  const { data: noteData } = useQuery({
    queryKey: ['note', noteId],
    queryFn: async (): Promise<Note> => {
      const response = await apiFetch(`/v1/note/${noteId}`)
      return response.json()
    },
    enabled: !!noteId,
  })

  const isNoteEnded = useMemo(() => {
    return noteData?.endAt ? !dayjs().isBefore(noteData.endAt, 'day') : false
  }, [noteData?.endAt])

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

  // 노트 참여자(유저) 정보 가져오기
  const { data: noteUsers } = useQuery({
    queryKey: ['noteUsers', noteId],
    queryFn: async (): Promise<User[]> => {
      if (!noteId) return []
      const response = await apiFetch(`/v1/note/${noteId}/users`)
      return response.json()
    },
    enabled: !!noteId,
    retry: 0,
  })

  // 이전 채팅기록 useQuery로 불러오기
  const { data: chatHistory } = useQuery({
    queryKey: ['chatHistory', noteId, activeFilter],
    queryFn: async (): Promise<ChatMessageResponse[]> => {
      if (!noteId) return []
      const url = activeFilter
        ? `/v1/chat/messages?noteId=${noteId}&messageType=${activeFilter}`
        : `/v1/chat/messages?noteId=${noteId}`
      const response = await apiFetch(url)
      const data = await response.json()
      return data.messages || []
    },
    enabled: !!noteId,
    retry: 0,
  })

  // noteUsers가 로드되면 userMap 생성
  useEffect(() => {
    if (noteUsers) {
      const map = new Map<number, User>()
      noteUsers.forEach(user => {
        map.set(user.id, user)
      })
      setUserMap(map)
    }
  }, [noteUsers])

  // 채팅 기록이 변경되면 메시지 업데이트
  useEffect(() => {
    if (chatHistory) {
      setMessages(chatHistory)
    }
  }, [chatHistory])

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

  // 메시지가 추가될 때마다 스크롤을 최하단으로 이동
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  function handleUserClick(userId: number) {
    const user = userMap.get(userId)
    if (user) {
      setSelectedUser(user)
      setOpenUserDialog(true)
    }
  }

  function handleSendMessage(content: string, type: MessageType) {
    sendMessage(content, type)
  }

  function handleFilterChange(filter: FilterType) {
    setActiveFilter(prev => (prev === filter ? undefined : filter))
  }

  return (
    <div className="flex flex-col h-screen bg-[#FAFAFA]">
      <header className="flex justify-between px-4 py-3">
        <button>
          <ChevronLeft onClick={() => navigate(-1)} />
        </button>
        <div className="flex gap-4">
          <div className="text-sm text-gray-500">
            {noteData?.endAt ? dayjs.utc(noteData.endAt).local().format('YYYY.MM.DD') : ''}
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
        <Filter activeFilter={activeFilter} setActiveFilter={handleFilterChange} />
        {messages.map(message => (
          <Message
            key={message.messageId}
            onUserClick={handleUserClick}
            user={userMap.get(message.userId)}
            {...message}
          />
        ))}
        <div ref={messagesEndRef} />
        <UserDialog user={selectedUser} open={openUserDialog} setOpen={setOpenUserDialog} />
      </main>

      <footer className="p-2">
        {!isNoteEnded && <ChatInput onSendMessage={handleSendMessage} disabled={!isConnected} />}
        {!isConnected && (
          <div className="text-center text-sm text-gray-500 px-4">
            {isConnecting ? '연결 중...' : '연결되지 않음'}
          </div>
        )}
      </footer>
    </div>
  )
}

import { ChevronLeft, Search } from 'lucide-react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { Dialog, DialogContent } from '@/components/ui/dialog'
import { ChatMessageResponse, MessageType } from '@/types/chat'

import { ChatInput } from './ChatInput'
import { FilterType } from './const'
import { DownloadButton } from './DownloadButton'
import { Filter } from './Filter'
import { Message } from './Message'

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
  const [activeFilter, setActiveFilter] = useState<FilterType>()
  const navigate = useNavigate()

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
        <Dialog>
          {dummyMessages.map(message => (
            <Message key={message.messageId} {...message} />
          ))}
          {/* 프로필 정보 */}
          <DialogContent className="rounded-2xl p-6 w-[360px] bg-white border-none">
            <div className="flex flex-col items-center space-y-6">
              {/* 프로필 이미지 */}
              <div className="w-24 h-24 rounded-md bg-muted" />

              {/* 정보 목록 */}
              <div className="w-full space-y-4 text-sm">
                <div className="space-y-4 text-sm">
                  <div className="flex">
                    <span className="w-20 font-medium">이메일</span>
                    <span className="text-muted-foreground">yayaya@naver.com</span>
                  </div>
                  <div className="flex">
                    <span className="w-20 font-medium">닉네임</span>
                    <span className="">야오 야옹</span>
                  </div>
                  <div className="flex">
                    <span className="w-20 font-medium">소개</span>
                    <span className="">야오 야옹야아오오오이이잉</span>
                  </div>
                </div>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </main>

      <footer className="p-2">
        <ChatInput />
      </footer>
    </div>
  )
}

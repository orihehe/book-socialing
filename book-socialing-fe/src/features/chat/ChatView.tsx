import { ChevronLeft, EllipsisVertical, Search } from 'lucide-react'

import { Input } from '@/components/ui/input'
import { MessageType } from '@/types/chat'

import { Message } from './Message'

export default function ChatPage() {
  return (
    <div className="flex flex-col h-screen bg-[#FAFAFA]">
      <header className="flex justify-between px-4 py-3">
        <button>
          <ChevronLeft />
        </button>
        <div className="flex gap-1">
          <div className="text-sm text-gray-500">2025.04.02</div>
          <h1 className="text-lg font-bold">빛과 실</h1>
        </div>
        <div className="flex gap-1">
          <button>
            <Search />
          </button>
          <button>
            <EllipsisVertical />
          </button>
        </div>
      </header>

      <main className="flex-1 overflow-y-auto px-4 py-2 space-y-4">
        <Message
          name="유저1"
          text="9월 9일 오후 8시 서울역 카페에서 만나요\n~^^"
          time="7:00"
          type={MessageType.NOTICE}
        />

        <Message name="유저2" text="반전 미쳤다" time="7:06" type={MessageType.REVIEW} />

        <Message
          name="유저2"
          text="근데 내가 이 상황이었다면?"
          time="9:10"
          type={MessageType.QUESTION}
        />

        <Message name="유저3" text="너무 감동적 p.45" time="11:11" type={MessageType.REVIEW} />
      </main>

      <footer className="p-2 border-t">
        <div className="flex flex-wrap gap-2 mb-2">
          {['공지', '질문', '감상', '자유', '내글만보기'].map(tag => (
            <button key={tag} className="bg-[#F2F4F6] text-gray-700 text-xs px-2 py-1 rounded-full">
              {tag}
            </button>
          ))}
        </div>
        <Input />
      </footer>
    </div>
  )
}

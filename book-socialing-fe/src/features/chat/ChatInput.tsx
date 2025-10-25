import { Send } from 'lucide-react'
import { useState } from 'react'

import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { MessageType } from '@/types/chat'

import { MESSAGE_TYPE_LABELS } from './const'

interface ChatInputProps {
  onSendMessage?: (content: string, type: MessageType) => void
  disabled?: boolean
}

const MessagePlaceholder = {
  [MessageType.NOTICE]: '공지사항을 입력해 주세요.',
  [MessageType.QUESTION]: '책에 대해 궁금한 내용을 입력해 주세요.',
  [MessageType.REVIEW]: '감상을 입력해 주세요.',
  [MessageType.GENERAL]: '생각을 입력해 주세요.',
}

export function ChatInput({ onSendMessage, disabled = false }: ChatInputProps) {
  const [messageType, setMessageType] = useState(MessageType.GENERAL)
  const [message, setMessage] = useState('')

  function handleMessageTypeChange(value: MessageType) {
    setMessageType(value)
  }

  function handleSend() {
    if (!message.trim() || disabled) return

    onSendMessage?.(message.trim(), messageType)
    setMessage('')
  }

  function handleKeyPress(e: React.KeyboardEvent) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="flex items-center gap-3 px-4 py-2 bg-gray-100 rounded-2xl mx-4 mb-4">
      {/* 입력 영역 */}
      <div className="flex-1 flex items-center gap-2">
        <Select value={messageType} onValueChange={handleMessageTypeChange}>
          <SelectTrigger className="border-0 shadow-none bg-transparent p-0 h-auto">
            <SelectValue defaultValue={MessageType.GENERAL} />
          </SelectTrigger>
          <SelectContent className="border-0">
            <SelectItem value={MessageType.NOTICE}>
              {MESSAGE_TYPE_LABELS[MessageType.NOTICE]}
            </SelectItem>
            <SelectItem value={MessageType.QUESTION}>
              {MESSAGE_TYPE_LABELS[MessageType.QUESTION]}
            </SelectItem>
            <SelectItem value={MessageType.REVIEW}>
              {MESSAGE_TYPE_LABELS[MessageType.REVIEW]}
            </SelectItem>
            <SelectItem value={MessageType.GENERAL}>
              {MESSAGE_TYPE_LABELS[MessageType.GENERAL]}
            </SelectItem>
          </SelectContent>
        </Select>

        <input
          value={message}
          onChange={e => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder={MessagePlaceholder[messageType]}
          disabled={disabled}
          className="flex-1 border-0 shadow-none focus:border-0 focus:ring-0 focus:outline-none bg-transparent placeholder:text-gray-500 disabled:opacity-50"
        />
        <Button
          size="icon"
          variant="ghost"
          onClick={handleSend}
          disabled={!message.trim() || disabled}
        >
          <Send />
        </Button>
      </div>
    </div>
  )
}

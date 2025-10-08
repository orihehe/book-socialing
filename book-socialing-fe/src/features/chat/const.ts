import { MessageType } from '@/types/chat'

export const FILTER_TYPE = {
  NOTICE: MessageType.NOTICE,
  REVIEW: MessageType.REVIEW,
  QUESTION: MessageType.QUESTION,
  GENERAL: MessageType.GENERAL,
  My: 'My',
}

export type FilterType = (typeof FILTER_TYPE)[keyof typeof FILTER_TYPE]

export const MESSAGE_TYPE_LABELS: Record<MessageType, string> = {
  [MessageType.NOTICE]: '공지',
  [MessageType.REVIEW]: '감상',
  [MessageType.QUESTION]: '질문',
  [MessageType.GENERAL]: '자유',
}

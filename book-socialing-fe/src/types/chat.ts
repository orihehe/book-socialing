export enum MessageType {
  NOTICE = 'NOTICE',
  REVIEW = 'REVIEW',
  QUESTION = 'QUESTION',
  GENERAL = 'GENERAL',
}

export type ChatMessageResponse = {
  messageId: number
  senderNickname: string
  content: string
  type: MessageType
  sentAt: string
}

export type ChatMessageRequest = {
  content: string
  type: MessageType
  emojis: string[]
}

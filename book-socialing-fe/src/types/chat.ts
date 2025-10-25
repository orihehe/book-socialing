export enum MessageType {
  NOTICE = 'NOTICE',
  REVIEW = 'REVIEW',
  QUESTION = 'QUESTION',
  GENERAL = 'GENERAL',
}

export type ChatMessageResponse = {
  messageId: number
  userId: number
  content: string
  type: string
  sentAt: string
}

export type ChatMessageRequest = {
  noteId: number
  content: string
  type: string
}

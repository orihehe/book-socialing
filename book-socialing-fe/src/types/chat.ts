export enum MessageType {
  NOTICE = 'NOTICE',
  REVIEW = 'REVIEW',
  QUESTION = 'QUESTION',
  GENERAL = 'GENERAL',
}

export type ChatMessageResponse = {
  messageId: string
  senderNickname: string
  content: string
  type: MessageType
  sentAt: string
}

export interface User {
  id: number
  email: string
  nickname: string
}

export type UserRole = 'HOST' | 'GUEST'
export type UserNoteStatus =
  | 'JOINED'
  | 'PENDING_APPROVAL'
  | 'CANCEL'
  | 'REJECTED'
  | 'LEFT'
  | 'KICKED'

export interface UserDetail {
  role: UserRole
  user: User
}

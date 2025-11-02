export interface User {
  id: number
  email: string
  nickname: string
  description?: string
}

export type UserRole = 'HOST' | 'GUEST'
export type UserStatus = 'JOINED' | 'PENDING_APPROVAL' | 'CANCEL' | 'REJECTED' | 'LEFT' | 'KICKED'

export interface UserDetail {
  role: UserRole
  user: User
  status: UserStatus
}

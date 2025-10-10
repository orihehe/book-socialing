export interface User {
  id: number
  email: string
  nickname: string
}

export type UserRole = 'HOST' | 'GUEST'

export interface UserDetail {
  role: UserRole
  user: User
}

export interface Note {
  id: number
  clubName?: string
  bookName: string
  bookAuthor?: string
  bookImageUrl?: string
  description?: string
  participants?: Participant[]
  startAt: string
  endAt: string
}

export interface Club {
  id: number
  name: string
}

export interface ClubNotesGroup {
  clubId: number
  clubName: string
  notes: Note[]
}

export interface ClubNotesPageResponse<T = ClubNotesGroup> {
  totalCount: number
  groups: T[]
}

export type ParticipantRole = 'HOST' | 'GUEST'
export interface Participant {
  participantId: number
  userId: number
  role: ParticipantRole
  status: 'JOINED'
}

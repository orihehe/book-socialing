export interface Club {
  id: string
  name: string
  description: string
  memberCount: number
  isMyClub?: boolean
  isCreatedByMe?: boolean
}

export interface ClubSection {
  title: string
  count: number
  showViewAll?: boolean
  showActions?: boolean
  clubs: Club[]
}

export interface CreateClubCommand {
  name: string
  description: string
  images: string[]
}

export interface Club {
  id: string
  name: string
  description: string
  memberCount: number
  imageUrl?: string
  isCreatedByMe?: boolean
  isMyClub?: boolean
}

export interface ClubSection {
  title: string
  count: number
  clubs: Club[]
  showViewAll?: boolean
  showActions?: boolean
}

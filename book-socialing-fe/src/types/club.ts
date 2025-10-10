export interface Club {
  id: number
  clubName: string
  description: string
  memberCount: number
  clubImageUrls: string[]
  isMyClub?: boolean
  isCreatedByMe?: boolean
}

export interface ClubSection {
  title: string
  totalCount: number
  showViewAll?: boolean
  showActions?: boolean
  refetch?: () => void
  clubs: Club[]
}

export interface CreateClubCommand {
  clubName: string
  description: string
  images: File[]
}

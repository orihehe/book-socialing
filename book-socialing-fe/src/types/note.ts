export interface Note {
  id: number
  clubName?: string
  bookName: string
  bookAuthor: string
  bookImageUrl?: string
  description?: string
  startDateTime: string
  endDateTime: string
}

export interface Club {
  id: string
  name: string
}

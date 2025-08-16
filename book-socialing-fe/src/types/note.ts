export interface Note {
  id: string
  title: string
  author: string
  description?: string
  imageUrl?: string
  startDateTime: string
  endDateTime: string
}

export interface Club {
  id: string
  name: string
}

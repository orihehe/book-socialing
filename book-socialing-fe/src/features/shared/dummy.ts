import { ClubNotesPageResponse } from '@/types/note'

export const dummy: ClubNotesPageResponse = {
  totalCount: 26,
  groups: [
    {
      clubId: 1,
      clubName: 'saisai',
      notes: [
        {
          id: 1,
          bookName: '두 개의 탑',
          bookAuthor: 'JRR Tolkien',
          bookImageUrl: 'https://covers.openlibrary.org/b/id/8231856-L.jpg',
          description: '~클럽의 몇번째 책임입니다',
          participants: [
            {
              participantId: 28,
              userId: 1,
              role: 'HOST',
              status: 'JOINED',
            },
            {
              participantId: 29,
              userId: 3,
              role: 'HOST',
              status: 'JOINED',
            },
            {
              participantId: 23,
              userId: 5,
              role: 'HOST',
              status: 'JOINED',
            },
            {
              participantId: 3,
              userId: 7,
              role: 'HOST',
              status: 'JOINED',
            },
            {
              participantId: 4,
              userId: 73,
              role: 'HOST',
              status: 'JOINED',
            },
          ],
          startAt: '2025-06-22T10:00:00',
          endAt: '2025-06-28T12:00:00',
        },
      ],
    },
  ],
}

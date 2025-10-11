import { useQuery } from '@tanstack/react-query'
import { useParams } from 'react-router-dom'

import { Label } from '@/components/ui/label'
import { UserImage } from '@/features/shared/components/UserImage'
import { UserDetail } from '@/types/user'

export default function UserScroll() {
  const { id } = useParams<{ id: string }>()
  const { data: clubMembers = [] } = useQuery<UserDetail[]>({
    queryKey: ['club', id, 'members'],
    queryFn: async () => {
      const res = await fetch(`/api/v1/club/${id}/members`)
      if (!res.ok) throw new Error('클럽 멤버 정보를 불러오지 못했습니다.')
      return res.json()
    },
    enabled: !!id,
  })

  return (
    <>
      <div className="space-y-2 my-7">
        <Label className="text-base font-bold">멤버 {clubMembers.length}</Label>
        <div
          className="overflow-x-auto mt-2"
          style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
        >
          <div className="flex gap-3 w-max overflow-x-auto !scrollbar-hide touch-auto">
            {clubMembers.map(member => (
              <UserImage key={member.user.id} user={member.user} size={15} />
            ))}
          </div>
        </div>
      </div>
    </>
  )
}

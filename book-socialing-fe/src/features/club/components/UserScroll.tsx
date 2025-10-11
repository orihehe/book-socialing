import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { useParams } from 'react-router-dom'

import { Label } from '@/components/ui/label'
import { UserDialog } from '@/features/shared/components/UserDialog'
import { UserImage } from '@/features/shared/components/UserImage'
import { apiFetch } from '@/lib/api'
import type { User, UserDetail } from '@/types/user'

export default function UserScroll() {
  const [open, setOpen] = useState(false)
  const [selectedUser, setSelectedUser] = useState<User>()
  const { id } = useParams<{ id: string }>()
  const { data: clubMembers = [] } = useQuery<UserDetail[]>({
    queryKey: ['club', id, 'members'],
    queryFn: async () => {
      const res = await apiFetch(`/v1/club/${id}/members`)
      if (!res.ok) throw new Error('클럽 멤버 정보를 불러오지 못했습니다.')
      return res.json()
    },
    enabled: !!id,
  })

  function handleUserClick(user: User) {
    setOpen(true)
    setSelectedUser(user)
  }

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
              <UserImage
                key={member.user.id}
                user={member.user}
                size={15}
                onClick={() => handleUserClick(member.user)}
              />
            ))}
          </div>
        </div>
      </div>

      <UserDialog open={open} setOpen={setOpen} user={selectedUser} />
    </>
  )
}

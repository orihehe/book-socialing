import { useState } from 'react'

import { Label } from '@/components/ui/label'
import { UserDialog } from '@/features/shared/components/UserDialog'
import { UserImage } from '@/features/shared/components/UserImage'
import type { User, UserDetail } from '@/types/user'

export default function UserScroll({ clubMembers }: { clubMembers: UserDetail[] }) {
  const [open, setOpen] = useState(false)
  const [selectedUser, setSelectedUser] = useState<User>()
  const joinedUsers = clubMembers.filter(member => member.status === 'JOINED')

  function handleUserClick(user: User) {
    setOpen(true)
    setSelectedUser(user)
  }

  return (
    <>
      <div className="space-y-2 my-7">
        <Label className="text-base font-bold">멤버 {joinedUsers.length}</Label>
        <div
          className="overflow-x-auto mt-2"
          style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
        >
          <div className="flex gap-3 w-max overflow-x-auto !scrollbar-hide touch-auto">
            {joinedUsers.map(member => (
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

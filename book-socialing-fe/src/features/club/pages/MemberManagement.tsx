import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useQuery } from '@tanstack/react-query'
import { X } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import { BottomButton } from '@/components/common/BottomButton'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'
import { UserImage } from '@/features/shared/components/UserImage'
import { apiFetch } from '@/lib/api'
import { User, UserDetail } from '@/types/user'

const MemberListItem = ({ user, action }: { user: User; action: React.ReactNode }) => (
  <div
    className="flex items-center justify-between px-4 py-4 rounded-xl transition-colors
               hover:bg-black/[0.02]" // 옵션: 호버 배경 살짝
  >
    <div className="flex items-center gap-3">
      <UserImage user={user} />
      <span className="text-sm font-bold">{user.nickname}</span>
    </div>
    {action}
  </div>
)

export default function MemberManagement() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const { data: clubMembers = [] } = useQuery<UserDetail[]>({
    queryKey: ['clubMembers', id],
    queryFn: async () => {
      const res = await apiFetch(`/v1/club/${id}/members`)
      if (!res.ok) throw new Error('Failed to fetch club members')
      return res.json()
    },
  })

  const rejectMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: string }) => {
      const response = await apiFetch(`/v1/note/${noteId}/participants/${userId}/reject`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
      })
      if (!response.ok) throw new Error('Failed to reject participant')
      return response.json()
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['noteParticipants'] }),
  })

  const kickMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: string }) => {
      const response = await apiFetch(`/v1/note/${noteId}/participants/${userId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      })
      if (!response.ok) throw new Error('Failed to kick participant')
      return response.json()
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['noteParticipants'] }),
  })

  const handleReject = (userId: string) => {
    rejectMutation.mutate({ noteId: id as string, userId })
  }
  const handleKick = (userId: string) => {
    kickMutation.mutate({ noteId: id as string, userId })
  }

  const pendingUsers = clubMembers.filter(user => user.status === 'PENDING_APPROVAL')
  const approvedUsers = clubMembers.filter(user => user.status === 'JOINED')

  return (
    <div className="flex flex-col min-h-screen px-4 pt-4 pb-6 bg-background">
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-lg font-semibold">클럽 멤버 관리</h1>
        <Button
          variant="ghost"
          size="icon"
          className="text-muted-foreground text-xl"
          onClick={() => navigate(-1)}
        >
          <X />
        </Button>
      </div>

      <ScrollArea className="flex-1 space-y-8 mt-7">
        {/* Pending Section */}
        <section className="rounded-2xl bg-[#FBFBFB]">
          <h2 className="text-mx font-bold px-4 pt-4">신청멤버</h2>
          {pendingUsers.length > 0 ? (
            <div className="mt-2 px-2 pb-4 space-y-3">
              {pendingUsers.map(user => (
                <MemberListItem
                  key={`pending-${user.user.id}`}
                  user={user.user}
                  action={
                    <BaseButton
                      onClick={() => handleReject('user-id')}
                      disabled={rejectMutation.isPending}
                    >
                      승인
                    </BaseButton>
                  }
                />
              ))}
            </div>
          ) : (
            <div className="flex items-center justify-center py-10">
              <p className="text-gray-400 text-sm">신청한 멤버가 없습니다</p>
            </div>
          )}
        </section>

        {/* Approved Section */}
        <section className="rounded-2xl bg-[#FBFBFB] mt-8">
          <h2 className="text-mx font-bold px-4 pt-4">참가멤버</h2>
          {approvedUsers.length > 0 ? (
            <div className="mt-2 px-2 pb-4 space-y-3">
              {approvedUsers.map(user => (
                <MemberListItem
                  key={`approved-${user.user.id}`}
                  user={user.user}
                  action={
                    <div className="flex gap-2">
                      <BaseButton
                        onClick={() => handleKick('user-id')}
                        disabled={kickMutation.isPending}
                      >
                        승인취소
                      </BaseButton>
                      <BaseButton
                        onClick={() => handleKick('user-id')}
                        disabled={kickMutation.isPending}
                      >
                        강퇴
                      </BaseButton>
                    </div>
                  }
                />
              ))}
            </div>
          ) : (
            <div className="flex items-center justify-center py-10">
              <p className="text-gray-400 text-sm">참가 중인 멤버가 없습니다</p>
            </div>
          )}
        </section>
      </ScrollArea>

      {/* Save Button */}
      <BottomButton>저장</BottomButton>
    </div>
  )
}

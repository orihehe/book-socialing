import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { X } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'
import { UserImage } from '@/features/shared/components/UserImage'
import { apiFetch } from '@/lib/api'
import { User, UserDetail } from '@/types/user'

const GuestListItem = ({ user, action }: { user: User; action: React.ReactNode }) => (
  <div className="flex items-center justify-between px-4 py-3">
    <div className="flex items-center gap-3">
      <UserImage user={user} />
      <span className="text-sm font-bold">{user.nickname}</span>
    </div>
    {action}
  </div>
)

export default function GuestManagementPage() {
  const navigate = useNavigate()
  const query = useParams()
  const id = query.id as string
  const queryClient = useQueryClient()

  const { data: guestsData = [] } = useQuery<UserDetail[]>({
    queryKey: ['noteGuests', id],
    queryFn: async () => {
      const response = await apiFetch(`/v1/note/guests?noteId=${id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) {
        throw new Error('Failed to fetch note guests')
      }
      return response.json()
    },
  })

  // 승인 뮤테이션
  const approveMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: number }) => {
      const response = await apiFetch(`/v1/note/${noteId}/participants/${userId}/approve`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to approve participant')
      }

      return response.json()
    },
    onSuccess: () => {
      // 성공 시 쿼리 무효화하여 데이터 새로고침
      queryClient.invalidateQueries({ queryKey: ['noteParticipants'] })
    },
  })

  // 거절 뮤테이션
  const rejectMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: number }) => {
      const response = await apiFetch(`/v1/note/${noteId}/participants/${userId}/reject`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to reject participant')
      }

      return response.json()
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['noteParticipants'] })
    },
  })

  const kickMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: number }) => {
      const response = await apiFetch(`/v1/note/${noteId}/participants/${userId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to reject participant')
      }

      return response.json()
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['noteParticipants'] })
    },
  })

  const handleApprove = (userId: number) => {
    approveMutation.mutate({ noteId: id, userId })
  }

  const handleReject = (userId: number) => {
    rejectMutation.mutate({ noteId: id, userId })
  }

  const handleKick = (userId: number) => {
    kickMutation.mutate({ noteId: id, userId })
  }

  const pendingUsers = guestsData.filter(user => user.status === 'PENDING_APPROVAL')
  const approvedUsers = guestsData.filter(user => user.status === 'JOINED')

  return (
    <div className="flex flex-col min-h-screen px-4 pt-4 pb-6 bg-background">
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-lg font-semibold">게스트 관리</h1>
        <Button
          variant="ghost"
          size="icon"
          className="text-muted-foreground text-xl"
          onClick={() => navigate(-1)}
        >
          <X />
        </Button>
      </div>

      <ScrollArea className="flex-1 space-y-6">
        {/* Pending Section */}
        <section className="rounded-2xl bg-[#FBFBFB]">
          <h2 className="text-mx font-bold px-4 pt-4">대기</h2>
          {pendingUsers.length > 0 ? (
            pendingUsers.map(user => (
              <GuestListItem
                key={user.user.id}
                user={user.user}
                action={
                  <div className="flex gap-2">
                    <BaseButton
                      isActive
                      onClick={() => handleApprove(user.user.id)}
                      disabled={approveMutation.isPending}
                    >
                      {approveMutation.isPending ? '처리중...' : '승인'}
                    </BaseButton>
                    <BaseButton
                      onClick={() => handleReject(user.user.id)}
                      disabled={rejectMutation.isPending}
                    >
                      {rejectMutation.isPending ? '처리중...' : '거절'}
                    </BaseButton>
                  </div>
                }
              />
            ))
          ) : (
            <div className="flex items-center justify-center py-10">
              <p className="text-gray-400 text-sm">대기 중인 게스트가 없습니다</p>
            </div>
          )}
        </section>

        {/* Approved Section */}
        <section className="rounded-2xl bg-[#FBFBFB] mt-8">
          <h2 className="text-mx font-bold px-4 pt-4">승인</h2>
          {approvedUsers.length > 0 ? (
            approvedUsers.map(user => (
              <GuestListItem
                key={`approved-${user.user.id}`}
                user={user.user}
                action={
                  <BaseButton
                    onClick={() => handleKick(user.user.id)}
                    disabled={kickMutation.isPending}
                  >
                    {kickMutation.isPending ? '처리중...' : '취소'}
                  </BaseButton>
                }
              />
            ))
          ) : (
            <div className="flex items-center justify-center py-10">
              <p className="text-gray-400 text-sm">승인된 게스트가 없습니다</p>
            </div>
          )}
        </section>
      </ScrollArea>

      {/* Save Button */}
      <Button className="mt-6 bg-green-900 hover:bg-green-800 text-white rounded-xl py-6">
        변경사항 저장
      </Button>
    </div>
  )
}

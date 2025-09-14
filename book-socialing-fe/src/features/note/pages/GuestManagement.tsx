import { useMutation, useQueryClient } from '@tanstack/react-query'
import { X } from 'lucide-react'
import { useNavigate } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'

const GuestListItem = ({ name, action }: { name: string; action: React.ReactNode }) => (
  <div className="flex items-center justify-between px-4 py-3">
    <div className="flex items-center gap-3">
      <Avatar key={name} className="w-6 h-6 border border-white shadow-sm">
        {/* 나중에 user image 넣을 수 있음 */}
        <AvatarImage src={/* getUserImage(p.userId) */ undefined} />
        <AvatarFallback className="text-xs bg-red-400 text-white">
          {name[0]} {/* fallback: userId 끝자리 */}
        </AvatarFallback>
      </Avatar>
      <span className="text-sm font-bold">{name}</span>
    </div>
    {action}
  </div>
)

export default function GuestManagementPage() {
  const navigate = useNavigate()
  const pendingGuests = ['닉네임', '닉네임', '닉네임']
  const approvedGuests = ['닉네임', '닉네임', '닉네임']

  const queryClient = useQueryClient()

  // 승인 뮤테이션
  const approveMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: string }) => {
      const response = await fetch(`/api/note/v1/${noteId}/participants/${userId}/approve`, {
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
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: string }) => {
      const response = await fetch(`/api/note/v1/${noteId}/participants/${userId}/reject`, {
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

  // 강퇴 뮤테이션
  const kickMutation = useMutation({
    mutationFn: async ({ noteId, userId }: { noteId: string; userId: string }) => {
      const response = await fetch(`/api/note/v1/${noteId}/participants/${userId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to kick participant')
      }

      return response.json()
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['noteParticipants'] })
    },
  })

  const handleApprove = (userId: string) => {
    const noteId = 'your-note-id' // 실제 noteId 가져오기
    approveMutation.mutate({ noteId, userId })
  }

  const handleReject = (userId: string) => {
    const noteId = 'your-note-id' // 실제 noteId 가져오기
    rejectMutation.mutate({ noteId, userId })
  }

  const handleKick = (userId: string) => {
    const noteId = 'your-note-id' // 실제 noteId 가져오기
    kickMutation.mutate({ noteId, userId })
  }

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
          {pendingGuests.map((name, idx) => (
            <GuestListItem
              key={`pending-${idx}`}
              name={name}
              action={
                <div className="flex gap-2">
                  <BaseButton
                    isActive
                    onClick={() => handleApprove('user-id')}
                    disabled={approveMutation.isPending}
                  >
                    {approveMutation.isPending ? '처리중...' : '승인'}
                  </BaseButton>
                  <BaseButton
                    onClick={() => handleReject('user-id')}
                    disabled={rejectMutation.isPending}
                  >
                    {rejectMutation.isPending ? '처리중...' : '거절'}
                  </BaseButton>
                </div>
              }
            />
          ))}
        </section>

        {/* Approved Section */}
        <section className="rounded-2xl bg-[#FBFBFB] mt-8">
          <h2 className="text-mx font-bold px-4 pt-4">승인</h2>
          {approvedGuests.map((name, idx) => (
            <GuestListItem
              key={`approved-${idx}`}
              name={name}
              action={
                <BaseButton onClick={() => handleKick('user-id')} disabled={kickMutation.isPending}>
                  {kickMutation.isPending ? '처리중...' : '취소'}
                </BaseButton>
              }
            />
          ))}
        </section>
      </ScrollArea>

      {/* Save Button */}
      <Button className="mt-6 bg-green-900 hover:bg-green-800 text-white rounded-xl py-6">
        변경사항 저장
      </Button>
    </div>
  )
}

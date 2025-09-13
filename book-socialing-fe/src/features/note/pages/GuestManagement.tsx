import { X } from 'lucide-react'
import { useNavigate } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'

const GuestListItem = ({ name, action }: { name: string; action: React.ReactNode }) => (
  <div className="flex items-center justify-between px-4 py-3">
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 rounded-full bg-muted" />
      <span className="text-sm font-medium">{name}</span>
    </div>
    {action}
  </div>
)

export default function GuestManagementPage() {
  const navigate = useNavigate()
  const pendingGuests = ['닉네임', '닉네임', '닉네임']
  const approvedGuests = ['닉네임', '닉네임', '닉네임']

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
        <section className="rounded-2xl bg-muted/30">
          <h2 className="text-sm font-medium px-4 pt-4">대기</h2>
          {pendingGuests.map((name, idx) => (
            <GuestListItem
              key={`pending-${idx}`}
              name={name}
              action={
                <div className="flex gap-2">
                  <BaseButton isActive>승인</BaseButton>
                  <BaseButton>거절</BaseButton>
                </div>
              }
            />
          ))}
        </section>

        {/* Approved Section */}
        <section className="rounded-2xl bg-muted/30">
          <h2 className="text-sm font-medium px-4 pt-4">승인</h2>
          {approvedGuests.map((name, idx) => (
            <GuestListItem
              key={`approved-${idx}`}
              name={name}
              action={<BaseButton>취소</BaseButton>}
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

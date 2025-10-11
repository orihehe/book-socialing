import { Dispatch, SetStateAction } from 'react'

import { Dialog, DialogContent } from '@/components/ui/dialog'
import type { User } from '@/types/user'

interface Props {
  user?: User
  open: boolean
  setOpen: Dispatch<SetStateAction<boolean>>
}
export function UserDialog({ user, open, setOpen }: Props) {
  if (!user) {
    return null
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogContent className="rounded-2xl p-6 w-[360px] bg-white border-none">
        <div className="flex flex-col items-center space-y-6">
          {/* 프로필 이미지 */}
          <div className="w-24 h-24 rounded-md bg-muted" />

          {/* 정보 목록 */}
          <div className="w-full space-y-4 text-sm">
            <div className="space-y-4 text-sm">
              <div className="flex">
                <span className="w-20 font-medium">이메일</span>
                <span className="text-muted-foreground">{user.email}</span>
              </div>
              <div className="flex">
                <span className="w-20 font-medium">닉네임</span>
                <span className="">{user.nickname}</span>
              </div>
              <div className="flex">
                <span className="w-20 font-medium">소개</span>
                <span className="">야오 야옹야아오오오이이잉</span>
              </div>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}

import { EllipsisVertical } from 'lucide-react'

import { Sheet, SheetClose, SheetContent, SheetTrigger } from '@/components/ui/sheet'

export function DownloadButton() {
  return (
    <Sheet>
      <SheetTrigger asChild>
        <button>
          <EllipsisVertical />
        </button>
      </SheetTrigger>
      <SheetContent
        side="bottom"
        className="rounded-t-xl px-4 py-6 space-y-4 w-full max-w-none bg-white"
      >
        <div className="space-y-4 text-left">
          <SheetClose asChild>
            <button className="text-base font-medium w-full text-left">내 글만 보기</button>
          </SheetClose>
          <SheetClose asChild>
            <button className="text-base font-medium w-full text-left">다운로드 받기</button>
          </SheetClose>
        </div>
      </SheetContent>
    </Sheet>
  )
}

import { Loader2 } from 'lucide-react'

import { cn } from '@/lib/utils'

export function LoadingPage({ className }: { className?: string }) {
  return (
    <div className={cn('flex h-screen w-full items-center justify-center bg-white', className)}>
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  )
}

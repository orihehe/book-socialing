import { Loader2 } from 'lucide-react'

import { cn } from '@/lib/utils'

interface Props {
  className?: string
}

export function LoadingPage({ className }: Props) {
  return (
    <div className={cn('flex h-screen w-full items-center justify-center bg-white', className)}>
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  )
}

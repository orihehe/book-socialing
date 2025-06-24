import { ReactNode } from 'react'

import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

import type { ComponentProps } from 'react'

type BaseButtonProps = {
  children: ReactNode
  type?: 'primary'
} & ComponentProps<typeof Button>

export function BaseButton({ children, className, type, ...buttonProps }: BaseButtonProps) {
  return (
    <Button
      className={cn(
        type === 'primary'
          ? 'bg-main text-white rounded-full border-none'
          : 'bg-[#F7F8F9] text-main rounded-full border-none',
        'h-7 px-2 text-sm rounded-full bg-gray-100 text-main',
        className
      )}
      {...buttonProps}
    >
      <span className="font-bold">{children}</span>
    </Button>
  )
}

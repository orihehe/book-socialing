import { ReactNode } from 'react'

import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

import type { ComponentProps } from 'react'

type BaseButtonProps = {
  children: ReactNode
  isActive?: boolean
} & ComponentProps<typeof Button>

export function BaseButton({ children, className, isActive, ...buttonProps }: BaseButtonProps) {
  return (
    <Button
      className={cn(
        'h-7 p-3 text-sm rounded-full border-none min-w-0',
        isActive ? 'bg-main text-white' : 'bg-[#F7F8F9] text-main',
        className
      )}
      {...buttonProps}
    >
      <span className="font-bold">{children}</span>
    </Button>
  )
}

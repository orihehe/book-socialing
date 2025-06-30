// components/common/BaseCard.tsx
import { ReactNode } from 'react'

import { Card, CardHeader, CardTitle } from '@/components/ui/card'
import { cn } from '@/lib/utils'

type BaseCardProps = {
  children: ReactNode
  className?: string
  title?: string
}

export function BaseCard({ children, className, title }: BaseCardProps) {
  return (
    <Card
      className={cn(
        'border-none bg-white rounded-[18px] shadow-[0px_3px_17px_7px_#0000000A] p-3 m-4 font-medium',
        className
      )}
    >
      {title && (
        <CardHeader className="pt-4">
          <CardTitle className="text-base font-semibold">{title}</CardTitle>
        </CardHeader>
      )}
      {children}
    </Card>
  )
}

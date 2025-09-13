// components/common/BaseCard.tsx
import { ReactNode } from 'react'

import { Card } from '@/components/ui/card'
import { cn } from '@/lib/utils'

type BaseCardProps = {
  children: ReactNode
  className?: string
  title?: string
}

export function BaseCard({ children, className, title }: BaseCardProps) {
  return (
    <div className="mb-4">
      {/* Header outside the card */}
      {title && (
        <div className="mb-6 mt-8 mx-4">
          <h2 className="text-lg font-bold text-gray-900">{title}</h2>
        </div>
      )}

      {/* Card content */}
      <Card
        className={cn(
          'border-none bg-white rounded-[18px] shadow-[0px_3px_17px_7px_#0000000A] p-3 mx-4 font-medium',
          className
        )}
      >
        {children}
      </Card>
    </div>
  )
}

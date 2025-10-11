import { Link, useNavigate } from 'react-router-dom'

import { cn } from '@/lib/utils'

interface PageHeaderProps {
  title?: string
  showBack?: boolean
  className?: string
  children?: React.ReactNode
}

function HeaderBackButton() {
  const navigate = useNavigate()
  return (
    <button
      type="button"
      onClick={() => {
        if (window.history.length > 1) {
          navigate(-1)
        } else {
          window.close()
        }
      }}
      className="p-2 rounded hover:bg-gray-100 active:bg-gray-200 transition-colors"
      aria-label="뒤로가기"
    >
      <svg width="20" height="20" fill="none" viewBox="0 0 20 20">
        <path
          d="M12.5 15l-5-5 5-5"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    </button>
  )
}

export function PageHeader({ title, showBack, className, children }: PageHeaderProps) {
  return (
    <header className={cn('flex items-center h-14 px-4 w-full', className)}>
      <div className="flex items-center gap-2 mr-auto">
        {showBack && <HeaderBackButton />}
        {title && (
          <Link to="/">
            <h1 className="text-lg font-semibold">{title}</h1>
          </Link>
        )}
      </div>
      {children && <div className="ml-auto flex items-center">{children}</div>}
    </header>
  )
}

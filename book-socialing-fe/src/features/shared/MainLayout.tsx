import { CircleUserRound, Search } from 'lucide-react'
import { Link } from 'react-router-dom'

import LNB from '@/components/layout/LNB'
import { PageHeader } from '@/components/layout/PageHeader'

interface Props {
  pageHeader?: React.ReactNode
  children: React.ReactNode
  activeItemIndex?: number
  activeChildIndex?: number
  onChildTabChange?: (index: number) => void
}

const lnbItems = [
  {
    name: '클럽',
    key: 'club',
    children: [],
  },
  {
    name: '노트',
    key: 'note',
    children: [
      { key: 'all', name: '전체' },
      { key: 'open', name: '열린노트' },
      { key: 'closed', name: '닫힌 노트' },
    ],
  },
]

export function MainLayout({
  pageHeader,
  children,
  activeItemIndex = 0,
  activeChildIndex,
  onChildTabChange,
}: Props) {
  const hasToken = !!localStorage.getItem('accessToken')

  return (
    <>
      {pageHeader ?? (
        <PageHeader title="SAISAI">
          <div className="flex items-center">
            <Link to="/search" className="pr-3">
              <Search className="w-5 h-5" />
            </Link>
            <Link to={hasToken ? '/my' : '/sign-in'}>
              <CircleUserRound className="w-5 h-5" />
            </Link>
          </div>
        </PageHeader>
      )}

      <LNB
        activeItemIndex={activeItemIndex}
        items={lnbItems}
        activeChildIndex={activeChildIndex}
        onChildTabChange={onChildTabChange}
      />

      {children}
    </>
  )
}

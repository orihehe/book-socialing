import { CircleUserRound, Search } from 'lucide-react'

import LNB from '@/components/layout/LNB'
import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'

interface Props {
  pageHeader?: React.ReactNode
  children: React.ReactNode
  activeTab?: string
  onTabChange?: (key: string) => void
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

export function MainLayout({ pageHeader, children, activeTab, onTabChange }: Props) {
  return (
    <>
      {pageHeader ?? (
        <PageHeader title="SAISAI">
          <div className="flex items-center">
            <Button variant="ghost" size="icon">
              <Search />
            </Button>
            <Button variant="ghost" className="-ml-1">
              <CircleUserRound />
            </Button>
          </div>
        </PageHeader>
      )}

      <LNB items={lnbItems} activeTab={activeTab} onTabChange={onTabChange} />

      {children}
    </>
  )
}

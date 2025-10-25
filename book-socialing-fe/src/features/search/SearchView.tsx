import { useQuery } from '@tanstack/react-query'
import { CircleUserRound, Search } from 'lucide-react'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useSearchParams } from 'react-router-dom'

import LNB from '@/components/layout/LNB'
import { PageHeader } from '@/components/layout/PageHeader'
import { InputGroup, InputGroupAddon, InputGroupInput } from '@/components/ui/input-group'
const lnbItems = [
  {
    name: '전체',
    key: 'all',
  },
  {
    name: '클럽',
    key: 'club',
  },
  {
    name: '노트',
    key: 'note',
  },
]

type SearchTarget = 'all' | 'club' | 'note'

export default function SearchView() {
  const [searchTarget, setSearchTarget] = useState<SearchTarget>('all')
  const activeItemIndex = lnbItems.findIndex(item => item.key === searchTarget)

  const [searchValue, setSearchValue] = useState<string>()

  const {
    data: noteSearchData,
    isLoading: noteSearchLoading,
    error: noteSearchError,
  } = useQuery({
    queryKey: ['note-search', searchValue],
    queryFn: async () => {
      if (!searchValue) return []
      const resp = await fetch(`/v1/note/search?query=${encodeURIComponent(searchValue)}`)
      if (!resp.ok) throw new Error('서버 오류')
      return resp.json()
    },
    enabled: !!searchValue,
    retry: false,
  })

  return (
    <div>
      <PageHeader showBack>
        <div className="flex items-center">
          <Link to="/sign-in">
            <CircleUserRound className="w-5 h-5" />
          </Link>
        </div>
      </PageHeader>
      <InputGroup className="w-[90%] border-none shadow-none rounded-xl mx-5 bg-[#E7ECEC]">
        <InputGroupInput
          type="search"
          placeholder="검색어를 입력하세요..."
          className="border-none shadow-none focus:ring-0"
          onKeyDown={e => {
            if (e.key === 'Enter' && e.currentTarget.value) {
              setSearchValue(e.currentTarget.value)
            }
          }}
        />
        <InputGroupAddon>
          <button>
            <Search />
          </button>
        </InputGroupAddon>
      </InputGroup>
      <div className="w-full">
        {/* First Level Navigation */}
        <div className="border-b border-gray-200">
          <div className="flex space-x-8 px-6 pt-4">
            {lnbItems.map(item => (
              <button
                key={item.key}
                className={`text-sm font-semibold transition-colors ${
                  searchTarget === item.key
                    ? 'text-gray-900 border-b-2 border-gray-900 pb-1'
                    : 'text-gray-500 hover:text-gray-700'
                }`}
              >
                {item.name}
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

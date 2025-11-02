import { useQuery } from '@tanstack/react-query'
import { CircleUserRound, Search, ChevronRight } from 'lucide-react'
import { useState, useRef } from 'react'
import { Link } from 'react-router-dom'

import { PageHeader } from '@/components/layout/PageHeader'
import { InputGroup, InputGroupAddon, InputGroupInput } from '@/components/ui/input-group'
import { apiFetch } from '@/lib/api'
import { ClubSearchResult } from '@/types/club'
import { NoteSearchResult } from '@/types/note'

import { ClubListItem } from './ClubListItem'
import { NoteListItem } from './NoteListItem'

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

interface ClubSearchResponse {
  totalCount: number
  groups: ClubSearchResult[]
}

interface NoteSearchResponse {
  totalCount: number
  groups: NoteSearchResult[]
}

export default function SearchView() {
  const [searchTarget, setSearchTarget] = useState<SearchTarget>('all')
  const [searchValue, setSearchValue] = useState<string>('')
  const searchInputRef = useRef<HTMLInputElement>(null)
  const hasToken = !!localStorage.getItem('accessToken')

  const { data: clubSearchData, isLoading: clubSearchLoading } = useQuery<ClubSearchResponse>({
    queryKey: ['club-search', searchValue],
    queryFn: async () => {
      if (!searchValue) return { totalCount: 0, groups: [] }
      const resp = await apiFetch(`/v1/club/search?query=${encodeURIComponent(searchValue)}`)
      if (!resp.ok) throw new Error('서버 오류')
      return resp.json()
    },
    enabled: !!searchValue && (searchTarget === 'all' || searchTarget === 'club'),
    retry: false,
  })

  const { data: noteSearchData, isLoading: noteSearchLoading } = useQuery<NoteSearchResponse>({
    queryKey: ['note-search', searchValue],
    queryFn: async () => {
      if (!searchValue) return { totalCount: 0, groups: [] }
      const resp = await apiFetch(`/v1/note/search?query=${encodeURIComponent(searchValue)}`)
      if (!resp.ok) throw new Error('서버 오류')
      return resp.json()
    },
    enabled: !!searchValue && (searchTarget === 'all' || searchTarget === 'note'),
    retry: false,
  })

  const handleSearch = (value: string) => {
    setSearchValue(value)
  }

  const totalCount =
    (searchTarget === 'all'
      ? (clubSearchData?.totalCount || 0) + (noteSearchData?.totalCount || 0)
      : searchTarget === 'club'
        ? clubSearchData?.totalCount || 0
        : noteSearchData?.totalCount || 0) || 0

  const showClubSection = searchTarget === 'all' || searchTarget === 'club'
  const showNoteSection = searchTarget === 'all' || searchTarget === 'note'

  return (
    <div className="min-h-screen bg-white">
      <PageHeader showBack>
        <div className="flex items-center">
          <Link to={hasToken ? '/my' : '/sign-in'}>
            <CircleUserRound className="w-5 h-5" />
          </Link>
        </div>
      </PageHeader>
      <div className="px-5 pt-4 pb-4">
        <InputGroup className="w-full border-none shadow-none rounded-xl bg-[#E7ECEC]">
          <InputGroupInput
            ref={searchInputRef}
            type="search"
            placeholder="검색어를 입력하세요..."
            className="border-none shadow-none focus:ring-0"
            defaultValue={searchValue}
            onKeyDown={e => {
              if (e.key === 'Enter' && e.currentTarget.value) {
                handleSearch(e.currentTarget.value)
              }
            }}
          />
          <InputGroupAddon>
            <div className="flex items-center gap-2">
              <button
                onClick={() => {
                  if (searchInputRef.current?.value) {
                    handleSearch(searchInputRef.current.value)
                  }
                }}
              >
                <Search className="w-4 h-4" />
              </button>
            </div>
          </InputGroupAddon>
        </InputGroup>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <div className="flex space-x-8 px-6 pt-4">
          {lnbItems.map(item => (
            <button
              key={item.key}
              onClick={() => setSearchTarget(item.key as SearchTarget)}
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

      {/* Search Results or Empty State */}
      {!searchValue ? (
        <div className="flex items-center justify-center px-6 pt-20">
          <p className="text-gray-400 text-sm">검색어를 입력해 주세요.</p>
        </div>
      ) : (
        <div className="px-6 pt-4 pb-6">
          {/* Total Count */}
          <div className="mb-4 text-sm text-gray-700">{totalCount}개</div>

          {/* Club Section */}
          {showClubSection && (
            <div className="mb-6">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-base font-semibold text-gray-900">
                  클럽{' '}
                  <span className="text-sm font-normal text-gray-500">
                    {clubSearchData?.totalCount || 0}개
                  </span>
                </h2>
              </div>

              {clubSearchLoading ? (
                <div className="text-sm text-gray-500">검색 중...</div>
              ) : clubSearchData && clubSearchData.groups.length > 0 ? (
                <>
                  <div className="space-y-0">
                    {clubSearchData.groups.slice(0, 3).map(club => (
                      <ClubListItem key={club.id} club={club} />
                    ))}
                  </div>
                  {clubSearchData.groups.length > 3 && (
                    <button className="mt-3 text-sm text-gray-500 flex items-center">
                      클럽 검색 결과 더보기 <ChevronRight className="w-4 h-4 ml-1" />
                    </button>
                  )}
                </>
              ) : (
                <div className="text-sm text-gray-400 py-4">검색 결과가 없습니다</div>
              )}
            </div>
          )}

          {/* Note Section */}
          {showNoteSection && (
            <div>
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-base font-semibold text-gray-900">
                  노트{' '}
                  <span className="text-sm font-normal text-gray-500">
                    {noteSearchData?.totalCount || 0}개
                  </span>
                </h2>
              </div>

              {noteSearchLoading ? (
                <div className="text-sm text-gray-500">검색 중...</div>
              ) : noteSearchData && noteSearchData.groups.length > 0 ? (
                <>
                  <div className="space-y-0">
                    {noteSearchData.groups.slice(0, 3).map(note => (
                      <NoteListItem key={note.id} note={note} />
                    ))}
                  </div>
                  {noteSearchData.groups.length > 3 && (
                    <button className="mt-3 text-sm text-gray-500 flex items-center">
                      노트 검색 결과 더보기 <ChevronRight className="w-4 h-4 ml-1" />
                    </button>
                  )}
                </>
              ) : (
                <div className="text-sm text-gray-400 py-4">검색 결과가 없습니다</div>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  )
}

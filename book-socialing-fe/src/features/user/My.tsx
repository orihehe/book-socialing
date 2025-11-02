import { useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'

import { BaseCard } from '@/components/common/BaseCard'
import { PageHeader } from '@/components/layout/PageHeader'
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { useUser } from '@/hooks/useUser'

import MyCalendar from './MyNoteCalendar'

export default function My() {
  const { user, isLoading, hasToken } = useUser()
  const navigate = useNavigate()
  const profileUrl = ''

  useEffect(() => {
    if (!hasToken) {
      navigate('/sign-in')
    }
  }, [hasToken, navigate])

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <span className="sr-only">로딩 중...</span>
        <div className="flex justify-center">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-muted-foreground/30 border-t-primary" />
        </div>
      </div>
    )
  }

  if (!user) {
    return null
  }

  return (
    <>
      <PageHeader title="SAISAI" />

      <BaseCard title="개인 정보 관리">
        <div className="w-full max-w-sm p-6 bg-white rounded-xl">
          {/* 수정하기 버튼 */}
          <Link to="/my/edit">
            <Button
              variant="ghost"
              size="sm"
              className="absolute right-4 top-4 text-gray-500 text-xs hover:text-gray-800"
            >
              수정하기
            </Button>
          </Link>

          {/* 프로필 본문 */}
          <div className="flex flex-col items-center gap-4">
            {/* 프로필 이미지 */}
            <Avatar className="w-16 h-16 border border-gray-100">
              {profileUrl ? (
                <AvatarImage src={profileUrl} alt={user.nickname} />
              ) : (
                <AvatarFallback className="bg-gray-100 text-gray-400 text-sm">👤</AvatarFallback>
              )}
            </Avatar>

            {/* 정보 */}
            <div className="w-full text-sm space-y-1.5 pt-5">
              <div className="flex">
                <span className="w-16 font-medium text-gray-500">이메일</span>
                <span className="text-gray-800 break-all">{user.email}</span>
              </div>
              <div className="flex">
                <span className="w-16 font-medium text-gray-500">닉네임</span>
                <span className="text-gray-800">{user.nickname}</span>
              </div>
              <div className="flex items-start">
                <span className="w-16 font-medium text-gray-500">소개</span>
                <span className="text-gray-800 whitespace-pre-line">
                  {user.description ?? '나를 소개하는 글을 써보세요!'}
                </span>
              </div>
            </div>
          </div>
        </div>
      </BaseCard>

      <MyCalendar />
    </>
  )
}

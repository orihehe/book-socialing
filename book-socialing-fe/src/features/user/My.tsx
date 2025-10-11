import { BaseCard } from '@/components/common/BaseCard'
import { PageHeader } from '@/components/layout/PageHeader'
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { User } from '@/types/user'

import MyCalendar from './MyNoteCalendar'

export default function My() {
  const user: User = {
    nickname: '야옹야옹',
    email: 'yaong@naver.com',
    id: 4,
  }
  const profileUrl = ''
  const bio = '야오 야옹야아오오오이이잉'

  return (
    <>
      <PageHeader title="SAISAI" />

      <BaseCard title="개인 정보 관리">
        <div className="w-full max-w-sm p-6 bg-white rounded-xl">
          {/* 수정하기 버튼 */}
          <Button
            variant="ghost"
            size="sm"
            className="absolute right-4 top-4 text-gray-500 text-xs hover:text-gray-800"
            // onClick={onEdit}
          >
            수정하기
          </Button>

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
                <span className="text-gray-800 whitespace-pre-line">{bio}</span>
              </div>
            </div>
          </div>
        </div>
      </BaseCard>

      <MyCalendar />
    </>
  )
}

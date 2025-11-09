import { useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'sonner'

import { useUser } from '@/hooks/useUser'

export default function KakaoCallback() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const params = new URLSearchParams(window.location.search)
  const accessToken = params.get('accessToken')
  // 유저 정보 가져오기
  const { user, isError } = useUser()

  // accessToken 저장 및 user 쿼리 활성화
  useEffect(() => {
    if (accessToken) {
      localStorage.setItem('accessToken', accessToken)
      // 토큰 저장 후 user 쿼리를 즉시 활성화
      queryClient.invalidateQueries({ queryKey: ['user', 'me'] })
    }
  }, [accessToken, queryClient])

  // 유저 정보 로드 성공 시 홈으로 이동
  useEffect(() => {
    if (user) {
      navigate('/')
    }
  }, [user, navigate])

  // 에러 처리
  useEffect(() => {
    if (!accessToken) {
      toast.error('카카오 인증 코드가 없습니다.')
      navigate('/sign-in')
      return
    }

    if (isError) {
      toast.error('사용자 정보를 불러오는데 실패했습니다.')
      navigate('/sign-in')
    }
  }, [accessToken, isError, navigate])

  return (
    <div className="flex items-center justify-center min-h-screen">
      <p className="text-gray-600">카카오 로그인 중…</p>
    </div>
  )
}

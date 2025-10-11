import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'sonner'

export default function KakaoCallback() {
  const navigate = useNavigate()
  const params = new URLSearchParams(window.location.search)
  const accessToken = params.get('accessToken')

  useEffect(() => {
    if (!accessToken) {
      // code가 없으면 에러 처리
      toast.error('카카오 인증 코드가 없습니다.')
      return
    }

    localStorage.setItem('accessToken', accessToken)
    navigate('/')
  }, [accessToken, navigate])

  return <p>카카오 로그인 중…</p>
}

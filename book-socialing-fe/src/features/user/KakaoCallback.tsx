import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'sonner'

export default function KakaoCallback() {
  const navigate = useNavigate()
  const params = new URLSearchParams(window.location.search)
  const code = params.get('code')

  useEffect(() => {
    if (!code) {
      // code가 없으면 에러 처리
      toast.error('카카오 인증 코드가 없습니다.')
      return
    }

    localStorage.setItem('accessToken', code)
    navigate('/')
  }, [code, navigate])

  return <p>카카오 로그인 중…</p>
}

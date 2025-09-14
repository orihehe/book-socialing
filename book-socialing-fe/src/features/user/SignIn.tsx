import { useForm } from '@tanstack/react-form'
import { zodValidator } from '@tanstack/zod-form-adapter'
import { Mail, Lock, Search, UserRound, CircleUserRound } from 'lucide-react'
import { z } from 'zod'

import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'

const loginSchema = z.object({
  email: z.string().email('올바른 이메일 형식을 입력해주세요'),
  password: z.string().min(6, '비밀번호는 6자 이상 입력해주세요'),
})

export default function Login() {
  const form = useForm({
    defaultValues: {
      email: '',
      password: '',
    },
    validators: {
      onChange: loginSchema,
    },
    onSubmit: async ({ value }) => {
      console.log('Login data:', value)
      // TODO: 로그인 API 호출
    },
  })

  function requestKakaoLogin() {
    const handleLogin = () => {
      const apiBaseUrl = import.meta.env.VITE_API_BASE_URL
      const frontendUrl = import.meta.env.VITE_FRONTEND_URL
      const clientId = import.meta.env.VITE_KAKAO_CLIENT_ID

      console.log({ apiBaseUrl, frontendUrl, clientId })

      // 오타 수정: clident_id → client_id, redirect_url → redirect_uri
      const requestTokenUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${
        frontendUrl + '/auth/kakao/callback'
      }&response_type=code`

      console.log('Redirecting to:', requestTokenUrl)
      window.location.href = requestTokenUrl
    }

    return handleLogin()
  }
  //team-beat.tistory.com/15 [BEAT Team Blog:티스토리]

  return (
    <>
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
      <div className="min-h-screen flex flex-col items-center justify-start px-6 pt-10 bg-white">
        {/* Header */}

        {/* Slogan */}
        <div className="text-left w-full mb-6">
          <p className="text-md font-medium">책사이, 우리사이, 사이사이</p>
          <h2 className="text-3xl font-extrabold tracking-tight">
            SAISAI<span className="text-green-700">.</span>
          </h2>
        </div>

        {/* Login Form */}
        <form
          onSubmit={e => {
            e.preventDefault()
            e.stopPropagation()
            form.handleSubmit()
          }}
          className="w-full space-y-4"
        >
          <form.Field
            name="email"
            children={field => (
              <div>
                <label htmlFor="email" className="text-sm font-semibold">
                  이메일
                </label>
                <Input
                  id="email"
                  type="email"
                  placeholder="yayaya@naver.com"
                  className="mt-1 bg-[#F7F8F9] placeholder:text-gray-400"
                  value={field.state.value}
                  onChange={e => field.handleChange(e.target.value)}
                  onBlur={field.handleBlur}
                />
                {field.state.meta.isTouched && field.state.meta.errors && (
                  <p className="text-red-500 text-xs mt-1">{field.state.meta.errors[0]?.message}</p>
                )}
              </div>
            )}
          />

          <form.Field
            name="password"
            children={field => (
              <div>
                <label htmlFor="password" className="text-sm font-semibold">
                  비밀번호
                </label>
                <Input
                  id="password"
                  type="password"
                  placeholder="비밀번호를 입력해주세요."
                  className="mt-1 bg-[#F7F8F9] placeholder:text-gray-400"
                  value={field.state.value}
                  onChange={e => field.handleChange(e.target.value)}
                  onBlur={field.handleBlur}
                />
                {field.state.meta.isTouched && field.state.meta.errors && (
                  <p className="text-red-500 text-xs mt-1">{field.state.meta.errors[0]?.message}</p>
                )}
              </div>
            )}
          />

          <div className="text-right text-xs text-gray-400">계정을 잊으셨나요?</div>

          <Button
            type="submit"
            className="w-full bg-main text-white text-sm py-5 rounded-md hover:bg-green-900 transition"
            disabled={form.state.isSubmitting}
          >
            {form.state.isSubmitting ? '로그인 중...' : '로그인하기'}
          </Button>
        </form>

        {/* Social Login */}
        <div className="flex justify-center gap-4 mt-6">
          <Button variant="ghost" onClick={requestKakaoLogin}>
            카카오로 로그인
          </Button>
          {/* <button className="w-10 h-10 rounded-full bg-yellow-400 text-black flex items-center justify-center">
            폴
          </button> */}
        </div>

        <div className="mt-4 text-sm text-gray-500">회원가입 하기</div>
      </div>
    </>
  )
}

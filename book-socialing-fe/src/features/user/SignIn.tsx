import { zodResolver } from '@hookform/resolvers/zod'
import { Search, CircleUserRound } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

import { PageHeader } from '@/components/layout/PageHeader'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'

const loginSchema = z.object({
  email: z.string().email('올바른 이메일 형식을 입력해 주세요'),
  password: z.string().min(6, '비밀번호는 6자 이상 입력해 주세요'),
})
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

type LoginFormData = z.infer<typeof loginSchema>

export default function Login() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
    mode: 'onChange',
  })

  const onSubmit = async (data: LoginFormData) => {
    console.log('Login data:', data)
    // TODO: 로그인 API 호출
  }

  function requestKakaoLogin() {
    window.location.href = `${apiBaseUrl}/oauth2/authorization/kakao`
  }

  return (
    <>
      <PageHeader title="SAISAI" />
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
        <form onSubmit={handleSubmit(onSubmit)} className="w-full space-y-4">
          <div>
            <label htmlFor="email" className="text-sm font-semibold">
              이메일
            </label>
            <Input
              id="email"
              type="email"
              placeholder="yayaya@naver.com"
              className="mt-1 bg-[#F7F8F9] placeholder:text-gray-400"
              {...register('email')}
            />
            {errors.email && <p className="text-red-500 text-xs mt-1">{errors.email.message}</p>}
          </div>

          <div>
            <label htmlFor="password" className="text-sm font-semibold">
              비밀번호
            </label>
            <Input
              id="password"
              type="password"
              placeholder="비밀번호를 입력해 주세요"
              className="mt-1 bg-[#F7F8F9] placeholder:text-gray-400"
              {...register('password')}
            />
            {errors.password && (
              <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>
            )}
          </div>

          <div className="text-right text-xs text-gray-400">계정을 잊으셨나요?</div>

          <Button
            type="submit"
            className="w-full bg-main text-white text-sm py-5 rounded-md hover:bg-green-900 transition"
            disabled={isSubmitting}
          >
            {isSubmitting ? '로그인 중...' : '로그인하기'}
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

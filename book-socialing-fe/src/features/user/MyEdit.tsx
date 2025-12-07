import { zodResolver } from '@hookform/resolvers/zod'
import { ChevronLeft } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useForm, FormProvider } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { toast } from 'sonner'
import { z } from 'zod'

import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog'
import { ImageUploadField } from '@/features/shared/components/form/ImageUploadField'
import { InputField } from '@/features/shared/components/form/InputField'
import { TextareaField } from '@/features/shared/components/form/TextareaField'
import { useUser } from '@/hooks/useUser'
import { apiFetch } from '@/lib/api'
import { getImageUrl } from '@/util'

const profileSchema = z.object({
  image: z.array(z.instanceof(File)).min(0).max(1, '최대 1장까지만 등록할 수 있어요').optional(),
  email: z.string().email('올바른 이메일을 입력해 주세요'),
  nickname: z
    .string()
    .min(2, '2-8자 이하')
    .max(8, '2-8자 이하')
    .regex(/^[a-zA-Z0-9가-힣]+$/, '공백불가'),
  bio: z.string().max(30, '30자 이하로 입력해 주세요').optional(),
})

export type ProfileFormData = z.infer<typeof profileSchema>

export default function MyEdit() {
  const navigate = useNavigate()
  const { user, isLoading } = useUser()
  const [withdrawDialogOpen, setWithdrawDialogOpen] = useState(false)

  const form = useForm<ProfileFormData>({
    defaultValues: {
      image: [],
      email: '',
      nickname: '',
      bio: '',
    },
    resolver: zodResolver(profileSchema),
    mode: 'onChange',
  })

  const {
    handleSubmit,
    watch,
    reset,
    formState: { errors, touchedFields },
  } = form

  const bioValue = watch('bio') || ''
  const nicknameValue = watch('nickname') || ''

  // 유저 정보 로드 시 form에 설정
  useEffect(() => {
    const loadUserData = async () => {
      if (user) {
        let profileImageFile: File[] = []

        // 기존 이미지가 있으면 File로 변환
        if (user.imageUrl && user.imageUrl !== '/images/default_book_image.jpg') {
          try {
            const response = await fetch(getImageUrl(user.imageUrl))
            const blob = await response.blob()
            const fileName = user.imageUrl.split('/').pop() || 'profile.jpg'
            const file = new File([blob], fileName, { type: blob.type })
            profileImageFile = [file]
          } catch {
            toast.error('프로필 이미지를 불러오는데 실패했습니다.')
          }
        }

        reset({
          image: profileImageFile,
          email: user.email,
          nickname: user.nickname,
          bio: user.description || '',
        })
      }
    }

    loadUserData()
  }, [user, reset])

  const onSubmit = async (data: ProfileFormData) => {
    try {
      const formData = new FormData()

      // 새 이미지가 있으면 추가
      if (data.image?.[0]) {
        formData.append('image', data.image[0])
      }

      formData.append('nickname', data.nickname)

      if (data.bio) {
        formData.append('bio', data.bio)
      }

      await apiFetch('/v1/user/me', { method: 'PUT', body: formData })
      toast.success('프로필이 수정되었습니다.')
      navigate('/my')
    } catch {
      toast.error('프로필 수정에 실패했습니다.')
    }
  }

  const handleWithdraw = async () => {
    try {
      await apiFetch('/v1/user', { method: 'DELETE' })
      localStorage.removeItem('accessToken')
      toast.success('회원 탈퇴가 완료되었습니다.')
      navigate('/sign-in')
    } catch {
      toast.error('회원 탈퇴에 실패했습니다.')
    } finally {
      setWithdrawDialogOpen(false)
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p className="text-gray-600">로딩 중...</p>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-white pb-24">
      {/* Header */}
      <div className="flex items-center p-4 border-b border-gray-200">
        <button className="mr-4" onClick={() => navigate(-1)}>
          <ChevronLeft className="w-6 h-6" />
        </button>
        <h1 className="text-lg font-semibold">개인정보수정</h1>
      </div>

      <FormProvider {...form}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="p-4 space-y-6">
            {/* Profile Image */}
            <div>
              <h2 className="text-lg font-bold mb-3">프로필 사진</h2>
              <ImageUploadField name="profileImage" max={1} />
            </div>

            {/* Email (Read-only) */}
            <div>
              <h2 className="text-lg font-bold mb-2">이메일</h2>
              <div className="w-full px-3 py-2 text-sm border border-gray-200 rounded-md bg-gray-50 text-gray-500">
                {user?.email}
              </div>
            </div>

            {/* Nickname */}
            <div>
              <h2 className="text-lg font-bold mb-2">닉네임</h2>
              <InputField name="nickname" label="" placeholder="닉네임을 입력하세요" hideError />
              <div className="flex items-center gap-2 mt-2 text-xs">
                {nicknameValue && touchedFields.nickname && !errors.nickname ? (
                  <span className="text-green-600 flex items-center">
                    <svg className="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                      <path
                        fillRule="evenodd"
                        d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                        clipRule="evenodd"
                      />
                    </svg>
                    2-8자 이하
                  </span>
                ) : (
                  <span className="text-gray-400">2-8자 이하</span>
                )}

                {nicknameValue && touchedFields.nickname && !errors.nickname ? (
                  <span className="text-green-600 flex items-center">
                    <svg className="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                      <path
                        fillRule="evenodd"
                        d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                        clipRule="evenodd"
                      />
                    </svg>
                    한글/숫자 가능
                  </span>
                ) : (
                  <span className="text-gray-400">한글/숫자 가능</span>
                )}

                {errors.nickname && errors.nickname.message?.includes('공백') ? (
                  <span className="text-red-500">공백불가</span>
                ) : (
                  <span className="text-gray-400">공백불가</span>
                )}
              </div>
            </div>

            {/* Bio */}
            <div>
              <h2 className="text-lg font-bold mb-2">소개</h2>
              <TextareaField name="bio" label="" placeholder="자기소개를 입력하세요" />
              <div className="text-right text-xs text-gray-400 mt-1">{bioValue.length}/30 자</div>
            </div>
          </div>

          {/* Bottom Buttons */}
          <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 p-4 flex gap-3">
            <button
              type="button"
              onClick={() => setWithdrawDialogOpen(true)}
              className="flex-1 py-3 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 font-semibold"
            >
              탈퇴
            </button>
            <button
              type="submit"
              className="flex-[2] py-3 text-white bg-[#2D5F5D] rounded-lg hover:bg-[#244948] font-semibold"
            >
              변경하기
            </button>
          </div>
        </form>
      </FormProvider>

      {/* Withdraw Confirmation Dialog */}
      <AlertDialog open={withdrawDialogOpen} onOpenChange={setWithdrawDialogOpen}>
        <AlertDialogContent className="bg-white border-none w-[80vw]">
          <AlertDialogHeader>
            <AlertDialogTitle>정말 탈퇴하시겠습니까?</AlertDialogTitle>
            <AlertDialogDescription>
              탈퇴하시면 모든 데이터가 삭제되며 복구할 수 없습니다.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>취소</AlertDialogCancel>
            <AlertDialogAction onClick={handleWithdraw}>탈퇴</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  )
}

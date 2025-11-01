import { zodResolver } from '@hookform/resolvers/zod'
import { ChevronLeft } from 'lucide-react'
import { useState } from 'react'
import { useForm, FormProvider } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
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

const profileSchema = z.object({
  profileImage: z
    .array(z.instanceof(File))
    .min(0)
    .max(1, '최대 1장까지만 등록할 수 있어요')
    .optional(),
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
  const [withdrawDialogOpen, setWithdrawDialogOpen] = useState(false)

  const form = useForm<ProfileFormData>({
    defaultValues: {
      profileImage: [],
      email: 'yayaya@naver.com', // 실제로는 서버에서 가져온 데이터
      nickname: '',
      bio: '',
    },
    resolver: zodResolver(profileSchema),
    mode: 'onChange',
  })

  const {
    handleSubmit,
    watch,
    formState: { errors, touchedFields },
  } = form

  const bioValue = watch('bio') || ''
  const nicknameValue = watch('nickname') || ''

  const onSubmit = async (data: ProfileFormData) => {
    console.log('Form data:', data)
    // TODO: API 호출하여 프로필 업데이트
    // const formData = new FormData()
    // if (data.profileImage?.[0]) formData.append('profileImage', data.profileImage[0])
    // formData.append('nickname', data.nickname)
    // if (data.bio) formData.append('bio', data.bio)
    // await apiFetch('/v1/user/profile', { method: 'PUT', body: formData })
    // navigate('/my')
  }

  const handleWithdraw = () => {
    // TODO: 탈퇴 API 호출
    console.log('회원 탈퇴')
    setWithdrawDialogOpen(false)
    // navigate('/sign-in')
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
              <ImageUploadField name="profileImage" />
            </div>

            {/* Email (Read-only) */}
            <div>
              <h2 className="text-lg font-bold mb-2">이메일</h2>
              <div className="w-full px-3 py-2 text-sm border border-gray-200 rounded-md bg-gray-50 text-gray-500">
                yayaya@naver.com
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

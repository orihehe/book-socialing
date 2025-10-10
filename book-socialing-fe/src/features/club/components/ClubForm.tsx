import { zodResolver } from '@hookform/resolvers/zod'
import { ArrowLeft } from 'lucide-react'
import { useForm, FormProvider } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButton'
import { ImageUploadField } from '@/features/shared/components/form/ImageUploadField'
import { InputField } from '@/features/shared/components/form/InputField'
import { TextareaField } from '@/features/shared/components/form/TextareaField'
import type { CreateClubCommand } from '@/types/club'

const createClubSchema = z.object({
  clubName: z
    .string()
    .min(1, '클럽명을 입력해 주세요')
    .max(20, '20자 이하로 입력해 주세요')
    .regex(/^[a-zA-Z0-9가-힣 ]+$/, '특수문자를 입력할 수 없습니다'),
  description: z.string().min(10, '10자 이상 입력해 주세요'),
  images: z
    .array(z.instanceof(File))
    .min(1, '이미지 1장은 필수등록입니다')
    .max(10, '최대 10장까지 등록할 수 있습니다.'),
})

type ClubFormData = z.infer<typeof createClubSchema>

interface ClubFormProps {
  mode: 'create' | 'edit'
  clubDetail?: Partial<CreateClubCommand>
  onSubmit: (data: CreateClubCommand) => void | Promise<void>
  onCancel?: () => void
}

export default function ClubForm({ mode, clubDetail, onSubmit, onCancel }: ClubFormProps) {
  const navigate = useNavigate()

  const form = useForm<ClubFormData>({
    defaultValues: clubDetail ?? {
      clubName: '',
      description: '',
      images: [],
    },
    resolver: zodResolver(createClubSchema),
    mode: 'onChange',
  })

  const { handleSubmit } = form

  const handleCancel = () => {
    if (onCancel) {
      onCancel()
    } else {
      navigate(-1)
    }
  }

  const getTitle = () => (mode === 'create' ? '클럽 정보 작성' : '클럽 정보 수정')
  const getButtonText = () => (mode === 'create' ? '클럽 생성' : '클럽 수정')

  const onFormSubmit = async (data: ClubFormData) => {
    // images 는 File[]이므로, 최종적으로 string[]로 변환이 필요할 수 있습니다.
    // 백엔드 명세에 따라 수정 필요
    await onSubmit({
      ...data,
    } as CreateClubCommand)
  }

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="flex items-center p-4">
        <button onClick={handleCancel} className="p-2 -ml-2 hover:bg-gray-100 rounded-lg">
          <ArrowLeft className="w-5 h-5" />
        </button>
        <h1 className="ml-2 text-lg font-semibold">{getTitle()}</h1>
      </div>

      <FormProvider {...form}>
        <form onSubmit={handleSubmit(onFormSubmit)}>
          {/* Main Content */}
          <div className="p-4 space-y-6">
            <ImageUploadField name="images" max={10} />
            {/* Club Name Input */}
            <InputField name="clubName" label="클럽이름" placeholder="1~20자 특수문자 제외" />
            {/* Club Description Textarea */}
            <TextareaField name="description" label="클럽소개" placeholder="10자 이상" />
          </div>

          {/* Bottom Buttons */}
          {mode === 'create' ? (
            <BottomButton
              onClick={() => {}}
              type="submit"
              disabled={!form.formState.isValid}
              children={getButtonText()}
            />
          ) : (
            // <div className="fixed bottom-6 left-4 right-4 space-y-3">
            //   <button
            //     type="button"
            //     onClick={handleCancel}
            //     className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 py-4 text-base font-medium rounded-lg"
            //   >
            //     삭제하기
            //   </button>

            // </div>
            <BottomButton type="submit" disabled={!form.formState.isValid}>
              {getButtonText()}
            </BottomButton>
          )}

          {/* 하단 여백 - 고정 버튼 높이만큼 */}
          <div className="h-24"></div>
        </form>
      </FormProvider>
    </div>
  )
}

import { useForm } from '@tanstack/react-form'
import { Camera, ArrowLeft } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButtonl'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import type { CreateClubCommand } from '@/types/club'

const createClubSchema = z.object({
  name: z.string().min(1, '클럽명을 입력해주세요').max(20, '20자 이하로 입력해주세요'),
  description: z.string().min(10, '10자 이상 입력해주세요'),
  images: z.array(z.string()).min(1, '이미지 1장은 필수등록입니다'),
})

interface ClubFormProps {
  mode: 'create' | 'edit'
  initialData?: Partial<CreateClubCommand>
  onSubmit: (data: CreateClubCommand) => void | Promise<void>
  onCancel?: () => void
}

export default function ClubForm({ mode, initialData, onSubmit, onCancel }: ClubFormProps) {
  const navigate = useNavigate()

  const form = useForm({
    defaultValues: {
      name: initialData?.name || '',
      description: initialData?.description || '',
      images: initialData?.images || [],
    },
    onSubmit: async ({ value }) => {
      await onSubmit(value)
    },
    validators: {
      onChange: createClubSchema,
    },
  })

  const handleImageUpload = () => {
    // TODO: Implement image upload logic
    console.log('Image upload clicked')
  }

  const handleCancel = () => {
    if (onCancel) {
      onCancel()
    } else {
      navigate(-1)
    }
  }

  const getTitle = () => (mode === 'create' ? '클럽 정보 작성' : '클럽 정보 수정')
  const getButtonText = () => (mode === 'create' ? '클럽 생성' : '클럽 수정')

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="flex items-center p-4 border-b">
        <button onClick={handleCancel} className="p-2 -ml-2 hover:bg-gray-100 rounded-lg">
          <ArrowLeft className="w-5 h-5" />
        </button>
        <h1 className="ml-2 text-lg font-semibold">{getTitle()}</h1>
      </div>

      {/* Main Content */}
      <div className="p-4 space-y-6">
        {/* Image Upload Section */}
        <div className="space-y-3">
          <div className="flex items-center space-x-4">
            <div
              onClick={handleImageUpload}
              className="w-20 h-20 border-2 border-main/30 rounded-lg flex flex-col items-center justify-center cursor-pointer hover:border-main/50 transition-colors"
            >
              <Camera className="w-6 h-6 text-main/60" />
              <span className="text-xs text-gray-500 mt-1">0/10</span>
            </div>
            <div className="flex-1">
              <p className="text-sm text-red-500">이미지 1장은 필수등록입니다</p>
            </div>
          </div>
        </div>

        {/* Club Name Input */}
        <form.Field
          name="name"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">클럽명</h2>
              <Input
                placeholder="1~20자 특수문자 제외"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                className={field.state.meta.errors ? 'border-red-500' : ''}
              />
              {field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Club Description Textarea */}
        <form.Field
          name="description"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">클럽소개</h2>
              <Textarea
                placeholder="10자 이상 써주세요"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                className="min-h-24 resize-none"
              />
              {field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />
      </div>

      {/* Bottom Buttons */}
      {mode === 'create' ? (
        <BottomButton onClick={() => form.handleSubmit()} children={getButtonText()} />
      ) : (
        <div className="fixed bottom-6 left-4 right-4 space-y-3">
          <button
            onClick={handleCancel}
            className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 py-4 text-base font-medium rounded-lg"
          >
            삭제하기
          </button>
          <button
            onClick={() => form.handleSubmit()}
            className="w-full bg-main hover:bg-main/90 text-white py-4 text-base font-medium rounded-lg"
          >
            {getButtonText()}
          </button>
        </div>
      )}

      {/* 하단 여백 - 고정 버튼 높이만큼 */}
      <div className="h-24"></div>
    </div>
  )
}

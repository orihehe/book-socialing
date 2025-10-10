import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation } from '@tanstack/react-query'
import { ko } from 'date-fns/locale'
import dayjs from 'dayjs'
import { ChevronLeft } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useForm, FormProvider } from 'react-hook-form'
import { useNavigate, useParams } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButton'
import { Calendar } from '@/components/ui/calendar'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { ImageUploadField } from '@/features/shared/components/form/ImageUploadField'
import { InputField } from '@/features/shared/components/form/InputField'
import { SelectField } from '@/features/shared/components/form/SelectField'
import { TextareaField } from '@/features/shared/components/form/TextareaField'
import type { Club } from '@/types/note'

// Mock data
const mockClubs: Club[] = [
  { id: 1, name: '독서클럽 A' },
  { id: 2, name: '독서클럽 B' },
  { id: 3, name: '독서클럽 C' },
]

const noteSchema = z.object({
  bookName: z
    .string()
    .min(1, '책이름을 입력해 주세요')
    .max(20, '20자 이하로 입력해 주세요')
    .regex(/^[a-zA-Z0-9가-힣 ]+$/, '특수문자를 입력할 수 없습니다'),

  bookAuthor: z
    .string()
    .min(1, '작가이름을 입력해 주세요')
    .max(20, '20자 이하로 입력해 주세요')
    .regex(/^[a-zA-Z0-9가-힣 ]+$/, '특수문자를 입력할 수 없습니다'),

  description: z.string().min(10, '10자 이상 입력해 주세요'),
  bookImages: z
    .array(z.instanceof(File))
    .min(1, '이미지는 최소 1장 이상 필요해요')
    .max(5, '최대 5장까지만 등록할 수 있어요'),
  startAt: z.string().min(1, '시작일을 선택해 주세요'),
  endAt: z.string().min(1, '종료일을 선택해 주세요'),
  clubId: z.union([z.number(), z.undefined()]),
})

type NoteFormData = z.infer<typeof noteSchema>

interface EditNoteProps {
  mode: 'create' | 'edit'
}

export default function EditNote({ mode }: EditNoteProps) {
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)
  const [calendarType, setCalendarType] = useState<'start' | 'end' | ''>('')

  const { id } = useParams<{ id: string }>()

  const form = useForm<NoteFormData>({
    defaultValues: {
      bookName: '',
      bookAuthor: '',
      description: '',
      bookImages: [],
      startAt: '',
      endAt: '',
      clubId: undefined,
    },
    resolver: zodResolver(noteSchema),
    mode: 'onChange',
  })
  const {
    handleSubmit,
    setValue,
    getValues,
    trigger,
    formState: { errors, touchedFields },
  } = form

  const mutation = useMutation({
    mutationFn: async (data: NoteFormData) => {
      const { bookImages, ...request } = data

      const formData = new FormData()
      const requestBlob = new Blob([JSON.stringify(request)], { type: 'application/json' })
      formData.append('request', requestBlob)
      bookImages.forEach(file => {
        formData.append('images', file, file.name)
      })

      const response = await fetch('/api/note/v1/create', { method: 'POST', body: formData })
      if (!response.ok) {
        throw new Error('Failed to create note')
      }
      return response.json()
    },
    onSuccess: () => {
      navigate('/note')
    },
    onError: error => {
      console.error('Error creating note:', error)
    },
  })

  useEffect(() => {
    // mode===edit, id 가 있으면 가져오기
  }, [mode, id])

  // Calendar field helpers
  const handleCalendarSelect = (date: Date | undefined) => {
    if (!date) return
    const iso = date.toISOString()
    if (calendarType === 'start') {
      setValue('startAt', iso, { shouldValidate: true, shouldDirty: true })
      trigger('startAt')
    } else if (calendarType === 'end') {
      setValue('endAt', iso, { shouldValidate: true, shouldDirty: true })
      trigger('endAt')
    }
    setOpen(false)
  }

  const onSubmit = (data: NoteFormData) => {
    mutation.mutate(data)
  }

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="flex items-center p-4 border-b border-gray-200">
        <button className="mr-4">
          <ChevronLeft onClick={() => navigate(-1)} />
        </button>
        <h1 className="text-lg font-semibold">노트 {mode === 'edit' ? '수정' : '생성'}</h1>
      </div>

      <FormProvider {...form}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="p-4 space-y-6">
            <ImageUploadField name="bookImages" />

            {/* Book Name */}
            <InputField name="bookName" label="책이름" placeholder="책이름" />

            {/* Author Name */}
            <InputField name="bookAuthor" label="작가이름" placeholder="작가이름" />

            {/* Club Selection */}
            <SelectField
              name="clubId"
              label="클럽"
              options={mockClubs.map(club => ({ value: club.id.toString(), label: club.name }))}
              coerceNumber
            />

            {/* Note Introduction */}
            <TextareaField name="description" label="노트소개" placeholder="10자 이상" />

            {/* Meeting Date Selection */}
            <div>
              <Label className="text-lg font-bold">
                모임일 선택
                <span className="text-xs text-gray-400 ml-2">
                  노트 탈고일은 일주일 뒤를 권고합니다.
                </span>
              </Label>
              <div className="flex w-full gap-10 my-2 mx-4">
                <button
                  type="button"
                  onClick={() => {
                    setCalendarType('start')
                    setOpen(true)
                  }}
                  className="flex flex-col items-start gap-1"
                >
                  <span className="text-lg font-bold">모임일</span>
                  {getValues('startAt') ? dayjs(getValues('startAt')).format('YYYY.MM.DD') : '선택'}
                </button>

                <button
                  type="button"
                  className="flex flex-col items-start gap-1"
                  onClick={() => {
                    setCalendarType('end')
                    setOpen(true)
                  }}
                >
                  <span className="text-lg font-bold">탈고일</span>
                  {getValues('endAt') ? dayjs(getValues('endAt')).format('YYYY.MM.DD') : '선택'}
                </button>
              </div>
              <Dialog open={open} onOpenChange={setOpen}>
                <DialogContent className="w-[340px] rounded-xl px-0 py-7 border-none bg-white">
                  <DialogHeader>
                    <DialogTitle>
                      {calendarType === 'start' ? '모임일 선택' : '탈고일 선택'}
                    </DialogTitle>
                  </DialogHeader>
                  <div className="w-full px-3">
                    <Calendar
                      locale={ko}
                      mode="single"
                      className="w-full px-3"
                      selected={
                        calendarType === 'start'
                          ? getValues('startAt')
                            ? new Date(getValues('startAt'))
                            : undefined
                          : getValues('endAt')
                            ? new Date(getValues('endAt'))
                            : undefined
                      }
                      onSelect={handleCalendarSelect}
                    />
                  </div>
                </DialogContent>
              </Dialog>
              <div>
                {touchedFields.startAt && errors.startAt && (
                  <p className="text-red-500 text-sm mt-2">{errors.startAt.message}</p>
                )}
                {touchedFields.endAt && errors.endAt && (
                  <p className="text-red-500 text-sm mt-2">{errors.endAt.message}</p>
                )}
              </div>
            </div>
          </div>
          {/* Bottom Button */}
          <BottomButton onClick={() => {}} children="노트 삭제하기" />
          <BottomButton type="submit" onClick={() => {}} children="노트 생성하기" />
        </form>
      </FormProvider>
    </div>
  )
}

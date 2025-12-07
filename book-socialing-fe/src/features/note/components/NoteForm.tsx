import { zodResolver } from '@hookform/resolvers/zod'
import { useQuery } from '@tanstack/react-query'
import { ko } from 'date-fns/locale'
import dayjs from 'dayjs'
import { ChevronLeft } from 'lucide-react'
import { useState, useEffect } from 'react'
import { useForm, FormProvider } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButton'
import { Calendar } from '@/components/ui/calendar'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { ImageUploadField } from '@/features/shared/components/form/ImageUploadField'
import { InputField } from '@/features/shared/components/form/InputField'
import { SelectField } from '@/features/shared/components/form/SelectField'
import { TextareaField } from '@/features/shared/components/form/TextareaField'
import { apiFetch } from '@/lib/api'
import type { ClubNotesPageResponse, Note } from '@/types/note'

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
    .min(1, '이미지는 필수에요')
    .max(1, '최대 1장까지만 등록할 수 있어요'),
  startAt: z.string().min(1, '시작일을 선택해 주세요'),
  endAt: z.string().min(1, '모임일을 선택해 주세요'),
  clubId: z.union([z.number(), z.undefined(), z.null()]),
})

export type NoteFormData = z.infer<typeof noteSchema>

interface Props {
  mode: 'create' | 'edit'
  note?: Note
  onSubmit: (data: NoteFormData) => void
}

export function NoteForm({ mode, note, onSubmit }: Props) {
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)

  const { data: clubNotesGroups } = useQuery<ClubNotesPageResponse, Error>({
    queryKey: ['clubs', 'created'],
    queryFn: async () => {
      const res = await apiFetch('/v1/club/created')
      return res.json()
    },
    staleTime: 5 * 60 * 1000,
  })

  const form = useForm<NoteFormData>({
    defaultValues: note ?? {
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
    formState: { errors, touchedFields, isSubmitted },
  } = form

  // Set startAt to today on mount
  useEffect(() => {
    if (!note) {
      const today = dayjs().startOf('day').toISOString()
      setValue('startAt', today, { shouldValidate: true })
    }
  }, [note, setValue])

  // Calendar field helpers
  const handleCalendarSelect = (date: Date | undefined) => {
    if (!date) return
    const iso = date.toISOString()
    setValue('endAt', iso, { shouldValidate: true, shouldDirty: true })
    trigger('endAt')
    setOpen(false)
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
            {!!clubNotesGroups?.groups?.length && (
              <SelectField
                name="clubId"
                label="클럽"
                options={[
                  { value: undefined, label: '미선택' },
                  ...clubNotesGroups.groups.map(club => ({
                    value: club.id.toString(),
                    label: club.clubName,
                  })),
                ]}
                coerceNumber
              />
            )}
            {/* Note Introduction */}
            <TextareaField name="description" label="노트소개" placeholder="10자 이상" />

            {/* Meeting Date Selection */}
            <div>
              <Label className="text-lg font-bold">
                모임일 선택
                <span className="text-xs text-gray-400 ml-2">
                  오늘부터 모임일까지의 기간을 선택해 주세요.
                </span>
              </Label>
              <div className="flex w-full gap-10 my-2 mx-4">
                <div className="flex flex-col items-start gap-1">
                  <span className="text-lg font-bold">시작일</span>
                  <span className="text-gray-700">
                    {getValues('startAt')
                      ? dayjs(getValues('startAt')).format('YYYY.MM.DD')
                      : dayjs().format('YYYY.MM.DD')}
                  </span>
                </div>

                <button
                  type="button"
                  className="flex flex-col items-start gap-1"
                  onClick={() => setOpen(true)}
                >
                  <span className="text-lg font-bold">모임일</span>
                  {getValues('endAt') ? dayjs(getValues('endAt')).format('YYYY.MM.DD') : '선택'}
                </button>
              </div>
              <Dialog open={open} onOpenChange={setOpen}>
                <DialogContent className="w-[340px] rounded-xl px-0 py-7 border-none bg-white">
                  <DialogHeader>
                    <DialogTitle className="ml-8">모임일 선택</DialogTitle>
                  </DialogHeader>
                  <div className="w-full px-3">
                    <Calendar
                      locale={ko}
                      mode="single"
                      className="w-full px-3"
                      selected={getValues('endAt') ? new Date(getValues('endAt')) : undefined}
                      onSelect={handleCalendarSelect}
                      disabled={date => dayjs(date).isBefore(dayjs(), 'day')}
                    />
                  </div>
                </DialogContent>
              </Dialog>
              <div>
                {(touchedFields.endAt || isSubmitted) && errors.endAt && (
                  <p className="text-red-500 text-sm mt-2">{errors.endAt.message}</p>
                )}
              </div>
            </div>
          </div>
          {/* Bottom Button */}
          <BottomButton
            type="submit"
            children={mode === 'edit' ? '노트 수정하기' : '노트 생성하기'}
          />
        </form>
      </FormProvider>
    </div>
  )
}

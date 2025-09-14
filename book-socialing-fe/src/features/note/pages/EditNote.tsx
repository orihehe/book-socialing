import { useForm } from '@tanstack/react-form'
import { useMutation } from '@tanstack/react-query'
import { ko } from 'date-fns/locale'
import dayjs from 'dayjs'
import { Camera, ChevronLeft, X } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButtonl'
import { Calendar } from '@/components/ui/calendar'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { cn } from '@/lib/utils'
import type { Club } from '@/types/note'

// Mock data
const mockClubs: Club[] = [
  { id: 1, name: '독서클럽 A' },
  { id: 2, name: '독서클럽 B' },
  { id: 3, name: '독서클럽 C' },
]

const noteSchema = z.object({
  bookName: z.string().min(1, '책이름을 입력해 주세요').max(20, '20자 이하로 입력해 주세요'),
  bookAuthor: z.string().min(1, '작가이름을 입력해 주세요').max(20, '20자 이하로 입력해 주세요'),
  description: z.string().min(10, '10자 이상 입력해 주세요'),
  bookImages: z
    .array(z.instanceof(File))
    .min(1, '이미지는 최소 1장 이상 필요해요')
    .max(5, '최대 5장까지만 등록할 수 있어요'),
  startDateTime: z.string().min(1, '시작일을 선택해 주세요'),
  endDateTime: z.string().min(1, '종료일을 선택해 주세요'),
  clubId: z.union([z.number(), z.undefined()]),
})

interface EditNoteProps {
  mode: 'create' | 'edit'
}

export default function EditNote({ mode }: EditNoteProps) {
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)
  const [calendarType, setCalendarType] = useState('')

  const { id } = useParams<{ id: string }>()
  type NoteFormData = z.infer<typeof noteSchema>

  const mutation = useMutation({
    mutationFn: async (data: NoteFormData) => {
      const { bookImages, ...request } = data

      const formData = new FormData()

      // request 데이터를 JSON으로 추가
      formData.append('request', JSON.stringify(request))

      // 이미지 파일들을 추가
      bookImages.forEach(file => {
        formData.append('images', file) // 각 파일을 개별적으로 추가
      })

      const response = await fetch('http://localhost:8080/api/note/v1/create', {
        method: 'POST',
        body: formData,
      })

      if (!response.ok) {
        throw new Error('Failed to create note')
      }

      return response.json()
    },
    onSuccess: data => {
      console.log('Note created successfully:', data)
      navigate('/notes')
    },
    onError: error => {
      console.error('Error creating note:', error)
    },
  })

  useEffect(() => {
    // mode===edit, id 가 있으면 가져오기
  }, [mode, id])

  const form = useForm({
    defaultValues: {
      bookName: '',
      bookAuthor: '',
      description: '',
      bookImages: [] as File[],
      startDateTime: '',
      endDateTime: '',
      clubId: undefined as number | undefined,
    },
    onSubmit: async ({ value }) => {
      console.log(value)
      mutation.mutate(value)
    },
    validators: {
      onChange: noteSchema,
    },
  })

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <div className="flex items-center p-4 border-b border-gray-200">
        <button className="mr-4">
          <ChevronLeft onClick={() => navigate(-1)} />
        </button>
        <h1 className="text-lg font-semibold">노트 생성</h1>
      </div>

      <div className="p-4 space-y-6">
        {/* Cover Images */}
        <form.Field
          name="bookImages"
          children={field => {
            const handleFiles = (files: FileList | null) => {
              console.log('hey')
              if (!files) return
              const newFiles = Array.from(files)
              const updated = [...field.state.value, ...newFiles].slice(0, 5) // 최대 5개 유지
              field.handleChange(updated)
            }
            return (
              <div>
                <Label htmlFor="picture" className="text-lg font-bold">
                  표지
                </Label>

                <div className="flex gap-4 mt-2">
                  {/* 업로드 버튼 */}
                  <div className="relative w-24 h-24 rounded-md bg-main/10 flex items-center justify-center border-2 border-main hover:bg-main/20 transition">
                    <Input
                      id="picture"
                      type="file"
                      accept="image/*"
                      multiple
                      className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
                      onChange={e => handleFiles(e.target.files)}
                    />
                    <Camera className="w-6 h-6 text-main pointer-events-none" />
                  </div>

                  {/* 업로드된 이미지들 */}
                  {field.state.value.map((file, i) => (
                    <div key={i} className="relative w-24 h-24">
                      <img
                        src={URL.createObjectURL(file)}
                        alt={`preview-${i}`}
                        className="w-full h-full object-cover rounded-md"
                      />
                      <button
                        type="button"
                        onClick={() => {
                          const next = [...field.state.value]
                          next.splice(i, 1)
                          field.handleChange(next)
                        }}
                        className="absolute -top-2 -right-2 bg-main text-white rounded-full w-5 h-5 flex items-center justify-center shadow-md"
                      >
                        <X className="w-3 h-3" />
                      </button>
                    </div>
                  ))}
                </div>

                {/* 에러 메시지 */}
                {field.state.meta.isTouched && field.state.meta.errors && (
                  <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
                )}
              </div>
            )
          }}
        />

        {/* Book Name */}
        <form.Field
          name="bookName"
          children={field => (
            <div>
              <Label className="text-lg font-bold">책이름</Label>
              <Input
                placeholder="책이름"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                onBlur={() => field.handleBlur()}
                className="w-full h-[35px] rounded-md mt-2 px-3 py-2.5 bg-[rgba(247,248,249,0.5)]"
              />
              {field.state.meta.isTouched && field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Author Name */}
        <form.Field
          name="bookAuthor"
          children={field => (
            <div>
              <Label className="text-lg font-bold">작가이름</Label>
              <Input
                type="text"
                placeholder="작가이름"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                onBlur={() => field.handleBlur()}
                className="w-full h-[35px] rounded-md mt-2 px-3 py-2.5 bg-[rgba(247,248,249,0.5)]"
              />
              {field.state.meta.isTouched && field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Club Selection */}
        <div>
          <form.Field
            name="clubId"
            children={field => (
              <div>
                <Label className="text-lg font-bold">클럽</Label>
                <Select
                  value={field.state.value !== undefined ? String(field.state.value) : undefined}
                  onValueChange={value =>
                    field.handleChange(value !== undefined ? Number(value) : undefined)
                  }
                >
                  <SelectTrigger
                    className={cn(
                      'w-full h-[35px] rounded-md px-3 py-2.5 mt-2 text-sm',
                      'bg-[rgba(247,248,249,0.5)] border border-[rgb(209,213,219)] text-foreground'
                    )}
                  >
                    <SelectValue placeholder="클럽 선택" className="text-[rgb(209,213,219)]" />
                  </SelectTrigger>

                  <SelectContent>
                    {mockClubs.map(club => (
                      <SelectItem key={club.id} value={String(club.id)}>
                        {club.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>

                {field.state.meta.isTouched && field.state.meta.errors?.[0] && (
                  <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0].message}</p>
                )}
              </div>
            )}
          />
        </div>

        {/* Note Introduction */}
        <form.Field
          name="description"
          children={field => (
            <div>
              <Label className="text-lg font-bold">노트소개</Label>
              <Textarea
                placeholder="10자 이상"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                onBlur={() => field.handleBlur()}
                className="w-full min-h-[100px] rounded-md mt-2 px-3 py-2.5 bg-[rgba(247,248,249,0.5)]"
              />
              {field.state.meta.isTouched && field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Meeting Date Selection */}

        <form.Field name="startDateTime">
          {startField => (
            <form.Field name="endDateTime">
              {endField => (
                <>
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
                      {startField.state.value
                        ? dayjs(startField.state.value).format('YYYY.MM.DD')
                        : '선택'}
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
                      {endField.state.value
                        ? dayjs(endField.state.value).format('YYYY.MM.DD')
                        : '선택'}
                    </button>
                  </div>

                  {/* 모달은 하나만 */}
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
                              ? new Date(startField.state.value)
                              : new Date(endField.state.value)
                          }
                          onSelect={date => {
                            if (!date) return
                            const iso = date.toISOString()
                            if (calendarType === 'start') startField.handleChange(iso)
                            else endField.handleChange(iso)
                            setOpen(false)
                          }}
                        />
                      </div>
                    </DialogContent>
                  </Dialog>
                </>
              )}
            </form.Field>
          )}
        </form.Field>
      </div>

      {/* Bottom Button */}
      <BottomButton onClick={() => form.handleSubmit()} children="노트 생성하기" />
    </div>
  )
}

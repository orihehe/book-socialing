import { useForm } from '@tanstack/react-form'
import { Camera, ChevronLeft, X } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { z } from 'zod'

import { BottomButton } from '@/components/common/BottomButtonl'
import { Calendar } from '@/components/ui/calendar'
import { Input } from '@/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import type { Club } from '@/types/note'

// Mock data
const mockClubs: Club[] = [
  { id: '1', name: '독서클럽 A' },
  { id: '2', name: '독서클럽 B' },
  { id: '3', name: '독서클럽 C' },
]

// Validation schema
const noteSchema = z.object({
  title: z.string().min(1, '책이름을 입력해주세요').max(20, '20자 이하로 입력해주세요'),
  author: z.string().min(1, '작가이름을 입력해주세요').max(20, '20자 이하로 입력해주세요'),
  description: z.string().min(10, '10자 이상 입력해주세요'),
  imageUrl: z.string().min(1, '이미지를 등록해주세요'),
  startDateTime: z.string().min(1, '시작일을 선택해주세요'),
  endDateTime: z.string().min(1, '종료일을 선택해주세요'),
})

export default function CreateNote() {
  const navigate = useNavigate()
  const form = useForm({
    defaultValues: {
      title: '',
      author: '',
      description: '',
      imageUrl: '',
      startDateTime: '',
      endDateTime: '',
    },
    onSubmit: async ({ value }) => {
      // TODO: Implement form submission
      console.log('Form submitted:', value)
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
          name="imageUrl"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">표지</h2>
              {field.state.value ? (
                <div className="relative">
                  <img
                    src={field.state.value}
                    alt="Book cover"
                    className="w-full h-32 object-cover rounded-lg"
                  />
                  <button
                    onClick={() => form.setFieldValue('imageUrl', '')}
                    className="absolute top-2 right-2 p-1 bg-red-500 text-white rounded-full hover:bg-red-600"
                  >
                    <X size={16} />
                  </button>
                </div>
              ) : (
                <div
                  className="w-full h-32 border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center cursor-pointer hover:border-gray-400"
                  onClick={() =>
                    field.handleChange('https://via.placeholder.com/300x200?text=Book+Cover')
                  }
                >
                  <div className="flex flex-col items-center text-gray-500 hover:text-gray-700">
                    <Camera size={24} className="mb-2" />
                    <span className="text-sm">이미지 추가</span>
                  </div>
                </div>
              )}
            </div>
          )}
        />
        <p className="text-red-500 text-sm mt-2">이미지 1장은 필수등록입니다</p>

        {/* Book Name */}
        <form.Field
          name="title"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">책이름</h2>
              <Input
                placeholder="1~20자 특수문자 제외"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
              />
              {field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Author Name */}
        <form.Field
          name="author"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">작가이름</h2>
              <Input
                type="text"
                placeholder="1~20자 특수문자 제외"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
              />
              {field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Club Selection */}
        <div>
          <h2 className="text-base font-medium mb-3">클럽 선택</h2>
          <Select onValueChange={value => console.log('Club selected:', value)}>
            <SelectTrigger className="w-full">
              <SelectValue placeholder="클럽선택" />
            </SelectTrigger>
            <SelectContent>
              {mockClubs.map(club => (
                <SelectItem key={club.id} value={club.id}>
                  {club.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {/* Note Introduction */}
        <form.Field
          name="description"
          children={field => (
            <div>
              <h2 className="text-base font-medium mb-3">노트소개</h2>
              <Textarea
                placeholder="10자 이상"
                value={field.state.value}
                onChange={e => field.handleChange(e.target.value)}
                className="min-h-[100px]"
              />
              {field.state.meta.errors && (
                <p className="text-red-500 text-sm mt-2">{field.state.meta.errors[0]?.message}</p>
              )}
            </div>
          )}
        />

        {/* Meeting Date Selection */}
        <div>
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-base font-medium">모임일 선택</h2>
            <p className="text-sm text-gray-500">노트 탈고일은 일주일 뒤를 권고합니다</p>
          </div>

          <div className="p-4 border border-gray-200 rounded-lg bg-gray-50">
            <div className="text-center font-semibold mb-3">25.07.15(화) → 25.07.22(화)</div>

            <Calendar
              mode="multiple"
              selected={[
                new Date(2025, 6, 15), // 7월 15일 (0-based month)
                new Date(2025, 6, 22), // 7월 22일
              ]}
              className="w-full rounded-md border-0 bg-transparent"
              classNames={{
                day_selected: 'bg-main text-white hover:bg-main/90 focus:bg-main',
                day_today: 'bg-blue-100 text-blue-600',
                head_cell: 'text-gray-500 font-medium',
                table: 'w-full',
                head_row: 'w-full',
                row: 'w-full',
                cell: 'w-full text-center p-0',
                day: 'h-16 w-full text-base hover:bg-gray-200 rounded-md flex items-center justify-center',
              }}
            />
          </div>
        </div>
      </div>

      {/* Bottom Button */}
      <BottomButton onClick={() => form.handleSubmit()} children="노트 생성하기" />
    </div>
  )
}

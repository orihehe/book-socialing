import { Camera, X } from 'lucide-react'
import { useEffect, useMemo } from 'react'
import { useFormContext, Controller } from 'react-hook-form'

import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

type Props = {
  name: string
  label?: string
  max?: number
}

export function ImageUploadField({ name, label = '이미지 업로드', max = 5 }: Props) {
  const {
    control,
    setValue,
    watch,
    formState: { errors },
  } = useFormContext()
  const watchedFiles = watch(name)
  const files: File[] = useMemo(() => watchedFiles ?? [], [watchedFiles])
  const errMsg = (errors as Record<string, { message: string }>)[name]?.message as
    | string
    | undefined

  // 이미지 URL 미리보기
  const previews = useMemo(() => files.map(f => URL.createObjectURL(f)), [files])
  useEffect(() => () => previews.forEach(url => URL.revokeObjectURL(url)), [previews])

  // 파일 추가
  const handleAddFiles = (list: FileList | null) => {
    if (!list) return
    const newFiles = Array.from(list)
    const updated = [...files, ...newFiles].slice(0, max)
    setValue(name, updated, { shouldValidate: true, shouldDirty: true })
  }

  // 특정 인덱스 삭제
  const handleRemove = (i: number) => {
    const next = [...files]
    next.splice(i, 1)
    setValue(name, next, { shouldValidate: true, shouldDirty: true })
  }

  return (
    <div>
      <Label htmlFor={`${name}-input`} className="text-lg font-bold">
        {label}
      </Label>

      <div className="flex gap-4 mt-2 flex-wrap">
        {/* 업로드 버튼 - max 개수 미만일 때만 표시 */}
        {files.length < max && (
          <div className="relative w-24 h-24 rounded-md bg-main/10 flex items-center justify-center border-2 border-main hover:bg-main/20 transition">
            <Input
              id={`${name}-input`}
              type="file"
              accept="image/*"
              multiple
              className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
              onChange={e => handleAddFiles(e.target.files)}
            />
            <Camera className="w-6 h-6 text-main pointer-events-none" />
          </div>
        )}

        {/* 이미지 미리보기 목록 */}
        {previews.map((src, i) => (
          <div key={i} className="relative w-24 h-24">
            <img src={src} alt={`preview-${i}`} className="w-full h-full object-cover rounded-md" />
            <button
              type="button"
              onClick={() => handleRemove(i)}
              className="absolute -top-2 -right-2 bg-main text-white rounded-full w-5 h-5 flex items-center justify-center shadow-md"
            >
              <X className="w-3 h-3" />
            </button>
          </div>
        ))}
      </div>

      {/* 에러 메시지 */}
      {errMsg && <p className="text-red-500 text-sm mt-2">{errMsg}</p>}

      {/* RHF 내부 등록용 Controller */}
      <Controller name={name} control={control} render={() => <></>} />
    </div>
  )
}

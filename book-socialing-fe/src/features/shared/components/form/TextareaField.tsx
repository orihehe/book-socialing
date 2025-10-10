import { useRef } from 'react'
import { useFormContext } from 'react-hook-form'

import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { cn } from '@/lib/utils'

type Props = {
  name: string
  label: string
  placeholder?: string
  rows?: number
  maxLength?: number
  showCount?: boolean // 글자수 표시
  className?: string
  disabled?: boolean
}

export function TextareaField({
  name,
  label,
  placeholder,
  rows = 4,
  maxLength,
  showCount = false,
  className,
  disabled,
}: Props) {
  const {
    register,
    setValue,
    watch,
    formState: { errors },
  } = useFormContext()

  // 에러 메시지
  const errMsg = (errors as Record<string, { message: string }>)[name]?.message as
    | string
    | undefined

  // 이 값이 undefined면 ''로 변환 (react-hook-form에서 빈 input의 value가 undefined로 넘어오는 경우 방지)
  let value: string = watch(name)
  if (typeof value !== 'string') value = value ?? ''

  const ref = useRef<HTMLTextAreaElement | null>(null)

  return (
    <div className="w-full">
      <Label htmlFor={name} className="text-lg font-bold">
        {label}
      </Label>

      <Textarea
        id={name}
        rows={rows}
        maxLength={maxLength}
        placeholder={placeholder}
        disabled={disabled}
        className={cn(
          'w-full h-[150px] rounded-md mt-2 px-3 py-2.5 bg-[rgba(247,248,249,0.5)]',
          errMsg && 'ring-1 ring-red-400',
          className
        )}
        {...register(name)}
        ref={el => {
          // RHF + 내부 ref 모두 연결
          ref.current = el
        }}
        value={value}
        onChange={e => {
          setValue(name, e.target.value, { shouldValidate: true, shouldDirty: true })
        }}
      />

      {/* 하단 보조 라인: 에러 or 카운트 */}
      <div className="mt-2 text-sm flex items-center justify-between">
        {errMsg ? <p className="text-red-500">{errMsg}</p> : <span />}

        {showCount && (
          <p className="text-gray-400">
            {value.length}
            {maxLength ? ` / ${maxLength}` : ''}
          </p>
        )}
      </div>
    </div>
  )
}

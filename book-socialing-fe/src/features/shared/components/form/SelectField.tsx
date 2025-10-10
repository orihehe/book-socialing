import { Controller, useFormContext } from 'react-hook-form'

import { Label } from '@/components/ui/label'
import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectContent,
  SelectItem,
} from '@/components/ui/select'
import { cn } from '@/lib/utils'

type Option = {
  value: string | undefined
  label: string
}

type Props = {
  name: string
  label: string
  options: Option[]
  coerceNumber?: boolean // 선택값을 숫자로 변환할지 여부
  className?: string
}

export function SelectField({ name, label, options, coerceNumber = false, className }: Props) {
  const {
    control,
    formState: { errors },
  } = useFormContext()

  const errMsg = (errors as Record<string, { message: string }>)[name]?.message as
    | string
    | undefined
    | undefined

  return (
    <div>
      <Label className="text-lg font-bold">{label}</Label>

      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <Select
            value={
              field.value !== undefined && field.value !== null ? String(field.value) : undefined
            }
            onValueChange={(v: string) => field.onChange(coerceNumber ? Number(v) : v)}
          >
            <SelectTrigger
              className={cn(
                'w-full h-[35px] rounded-md px-3 py-2.5 mt-2 text-sm',
                'bg-[rgba(247,248,249,0.5)] border border-[rgb(209,213,219)] text-foreground',
                className
              )}
            >
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {options.map(opt => (
                <SelectItem key={opt.value} value={opt.value!}>
                  {opt.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        )}
      />

      {errMsg && <p className="text-red-500 text-sm mt-2">{errMsg}</p>}
    </div>
  )
}

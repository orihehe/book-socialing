import { useFormContext } from 'react-hook-form'

import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

type Props = {
  name: string
  label: string
  placeholder?: string
  type?: string
  className?: string
  hideError?: boolean
}

export function InputField({
  name,
  label,
  placeholder,
  type = 'text',
  className,
  hideError = false,
}: Props) {
  const {
    register,
    formState: { errors },
  } = useFormContext()

  const errMsg = (errors as Record<string, { message: string }>)[name]?.message as
    | string
    | undefined

  return (
    <div>
      <Label htmlFor={name} className="text-lg font-bold">
        {label}
      </Label>

      <Input
        id={name}
        type={type}
        placeholder={placeholder}
        {...register(name)}
        className={`w-full h-[35px] rounded-md mt-2 px-3 py-2.5 bg-[rgba(247,248,249,0.5)] ${className ?? ''}`}
      />

      {!hideError && errMsg && <p className="text-red-500 text-sm mt-2">{errMsg}</p>}
    </div>
  )
}

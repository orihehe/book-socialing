import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

import type { CreateClubCommand } from '@/types/club'

import ClubForm from '../components/ClubForm'

export default function CreateClub() {
  const navigate = useNavigate()

  const mutation = useMutation({
    mutationFn: async (data: CreateClubCommand) => {
      const formData = new FormData()
      const { images, ...otherData } = data
      const requestBlob = new Blob([JSON.stringify(otherData)], { type: 'application/json' })
      formData.append('request', requestBlob)
      images.forEach(file => {
        formData.append('images', file, file.name)
      })

      const res = await fetch('/api/club/v1/create', {
        method: 'POST',
        body: formData,
      })

      if (!res.ok) {
        throw new Error('클럽 생성에 실패했습니다')
      }
      return res.json()
    },
    onSuccess: () => {
      navigate('/club')
    },
    onError: error => {
      // TODO: 오류 알림 등 처리
      console.error(error)
    },
  })

  const handleSubmit = async (data: CreateClubCommand) => {
    console.log(data)
    mutation.mutate(data)
  }

  return <ClubForm mode="create" onSubmit={handleSubmit} />
}

import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

import { apiFetch } from '@/lib/api'
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

      const res = await apiFetch('/v1/club/create', {
        method: 'POST',
        body: formData,
      })
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

  async function handleSubmit(data: CreateClubCommand) {
    mutation.mutate(data)
  }

  return <ClubForm mode="create" onSubmit={handleSubmit} />
}

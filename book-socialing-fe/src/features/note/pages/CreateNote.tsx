import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

import { apiFetch } from '@/lib/api'

import { NoteForm, type NoteFormData } from '../components/NoteForm'

export default function CreateNote() {
  const navigate = useNavigate()

  const mutation = useMutation({
    mutationFn: async (data: NoteFormData) => {
      const { bookImages, ...request } = data

      const formData = new FormData()
      const requestBlob = new Blob([JSON.stringify(request)], { type: 'application/json' })
      formData.append('request', requestBlob)
      bookImages.forEach(file => {
        formData.append('images', file, file.name)
      })

      const response = await apiFetch('/v1/note/create', { method: 'POST', body: formData })
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

  const handleSubmit = async (data: NoteFormData) => {
    console.log(data)
    mutation.mutate(data)
  }

  return <NoteForm mode="create" onSubmit={handleSubmit} />
}

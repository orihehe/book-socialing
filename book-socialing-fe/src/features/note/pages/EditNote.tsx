import { useMutation, useQuery } from '@tanstack/react-query'
import { useNavigate, useParams } from 'react-router-dom'
import { toast } from 'sonner'

import { BottomButton } from '@/components/common/BottomButton'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { getImageFile } from '@/util'

import { NoteForm, type NoteFormData } from '../components/NoteForm'

export default function EditNote() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const { data: noteData, isLoading } = useQuery({
    queryKey: ['note', id],
    queryFn: async () => {
      const response = await fetch(`/api/v1/note/${id}`)
      if (!response.ok) {
        throw new Error('Failed to fetch note data')
      }
      const note = await response.json()

      return { ...note, bookImages: await getImageFile(note.imageUrls?.[0]) }
    },
    enabled: !!id,
  })

  const mutation = useMutation({
    mutationFn: async (data: NoteFormData) => {
      const { bookImages, ...request } = data

      const formData = new FormData()
      const requestBlob = new Blob([JSON.stringify(request)], { type: 'application/json' })
      formData.append('request', requestBlob)
      bookImages.forEach(file => {
        formData.append('images', file, file.name)
      })

      const response = await fetch(`/api/v1/note/${id}`, { method: 'PUT', body: formData })
      if (!response.ok) {
        throw new Error('Failed to update note')
      }
      return true
    },
    onSuccess: () => {
      toast.success('수정되었습니다.', { position: 'top-center' })
      // navigate('/note')
    },
    onError: error => {
      console.error('Error updating note:', error)
    },
  })
  const deleteMutation = useMutation({
    mutationFn: async () => {
      const response = await fetch(`/api/v1/note/${id}`, { method: 'DELETE' })
      if (!response.ok) {
        throw new Error('Failed to delete note')
      }

      if (response.status === 204) {
        return null
      }

      return response.json()
    },
    onSuccess: () => {
      navigate('/note')
    },
    onError: error => {
      console.error('Error deleting note:', error)
    },
  })

  async function handleSubmit(data: NoteFormData) {
    mutation.mutate(data)
  }

  async function handleDelete() {
    deleteMutation.mutate()
  }

  if (isLoading) {
    return <LoadingPage />
  }

  return (
    <>
      <NoteForm note={noteData} mode="edit" onSubmit={handleSubmit} />

      <AlertDialog>
        <AlertDialogTrigger>
          <BottomButton children="노트 삭제하기" isSub />
        </AlertDialogTrigger>
        <AlertDialogContent className="bg-white border-none w-[80vw]">
          <AlertDialogHeader>
            <AlertDialogTitle>정말 삭제하시겠어요?</AlertDialogTitle>
            <AlertDialogDescription>
              삭제 후 복구할 수 없습니다.
              <br />
              계속 진행하시겠습니까?
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogAction>아니요</AlertDialogAction>
            <AlertDialogCancel onClick={handleDelete}>네</AlertDialogCancel>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  )
}

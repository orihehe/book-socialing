import { useMutation, useQuery } from '@tanstack/react-query'
import { useNavigate, useParams } from 'react-router-dom'

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
import { apiFetch } from '@/lib/api'
import type { CreateClubCommand } from '@/types/club'
import { getImageFile } from '@/util'

import ClubForm from '../components/ClubForm'
export default function EditClub() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const { data: clubDetail, isLoading } = useQuery<Partial<CreateClubCommand>>({
    queryKey: ['club', id],
    queryFn: async (): Promise<Partial<CreateClubCommand>> => {
      const res = await apiFetch(`/v1/club/${id}`)
      if (!res.ok) throw new Error('클럽 정보를 불러오지 못했습니다.')
      const club = await res.json()

      const images = (
        await Promise.all((club.clubImageUrls as string[]).map((url: string) => getImageFile(url)))
      ).flatMap(image => image)

      return { ...club, images }
    },
    enabled: !!id,
  })

  const deleteMutation = useMutation({
    mutationFn: async () => {
      const res = await apiFetch(`/v1/club/${id}`, { method: 'DELETE' })
      if (!res.ok) {
        throw new Error('클럽 삭제에 실패했습니다.')
      }
      if (res.status === 204) {
        return null
      }
      return res.json()
    },
    onSuccess: () => {
      navigate('/club')
    },
    onError: error => {
      console.error('Error deleting club:', error)
    },
  })

  async function handleSubmit(data: CreateClubCommand) {
    console.log('Updated club data:', data)
    // TODO: Implement club update logic
    navigate('/club')
  }

  function handleCancel() {
    navigate('/club')
  }

  if (isLoading) {
    return <LoadingPage />
  }

  return (
    <>
      {' '}
      <ClubForm
        mode="edit"
        clubDetail={clubDetail}
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
      <AlertDialog>
        <AlertDialogTrigger>
          <BottomButton children="클럽삭제" isSub />
        </AlertDialogTrigger>
        <AlertDialogContent className="bg-white border-none w-[80vw] rounded-lg">
          <AlertDialogHeader>
            <AlertDialogTitle>클럽삭제</AlertDialogTitle>
            <AlertDialogDescription>
              [{clubDetail?.clubName}]을 삭제하시겠습니까?
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>아니요</AlertDialogCancel>
            <AlertDialogAction onClick={() => deleteMutation.mutate()}>예</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  )
}

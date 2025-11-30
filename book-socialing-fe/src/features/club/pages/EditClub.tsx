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
import { apiFetch } from '@/lib/api'
import type { CreateClubCommand } from '@/types/club'
import { getImageUrl } from '@/util'

import ClubForm from '../components/ClubForm'
export default function EditClub() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const { data: clubDetail, isLoading } = useQuery<Partial<CreateClubCommand>>({
    queryKey: ['club', id],
    queryFn: async (): Promise<Partial<CreateClubCommand>> => {
      const res = await apiFetch(`/v1/club/${id}`)
      const club = await res.json()

      // URL을 File 객체로 변환
      const imageFiles = await Promise.all(
        (club.clubImageUrls || []).map(async (url: string) => {
          try {
            const imageUrl = getImageUrl(url)
            const response = await fetch(imageUrl)

            if (!response.ok) {
              console.error(`이미지 로딩 실패: ${imageUrl}, 상태: ${response.status}`)
              return null
            }

            const blob = await response.blob()
            const fileName = url.split('/').pop() || 'image.jpg'
            return new File([blob], fileName, { type: blob.type })
          } catch (error) {
            console.error(`이미지 로딩 에러: ${url}`, error)
            return null
          }
        })
      )

      return {
        clubName: club.clubName,
        description: club.description,
        images: imageFiles.filter((f): f is File => f !== null),
      }
    },
    enabled: !!id,
  })

  const deleteMutation = useMutation({
    mutationFn: async () => {
      const res = await apiFetch(`/v1/club/${id}`, { method: 'DELETE' })
      if (res.status === 204) {
        return null
      }
      return res.json()
    },
    onSuccess: () => {
      toast.success('삭제되었습니다.')
      navigate('/club')
    },
    onError: error => {
      toast.error('일시적인 에러가 발생하였습니다. 다시 시도해 주세요.')
      console.error('Error deleting club:', error)
    },
  })

  const updateMutation = useMutation({
    mutationFn: async (clubData: CreateClubCommand) => {
      const res = await apiFetch(`/v1/club/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(clubData),
      })
      return res.json()
    },
    onSuccess: () => {
      toast.success('클럽 정보가 수정되었습니다.')
      navigate('/club')
    },
    onError: error => {
      toast.error('클럽 수정에 실패했습니다. 다시 시도해 주세요.')
      console.error('Error updating club:', error)
    },
  })

  function handleCancel() {
    navigate('/club')
  }

  if (isLoading) {
    return <LoadingPage />
  }

  return (
    <>
      <ClubForm
        mode="edit"
        clubDetail={clubDetail}
        onSubmit={updateMutation.mutate}
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

import { useNavigate, useParams } from 'react-router-dom'

import type { CreateClubCommand } from '@/types/club'

import ClubForm from '../components/ClubForm'

export default function EditClub() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  console.log(id)

  // TODO: Fetch club data by ID
  const mockClubData: Partial<CreateClubCommand> = {
    name: '기존 클럽명',
    description: '기존 클럽 소개입니다. 10자 이상의 텍스트가 들어갑니다.',
    images: ['image1.jpg', 'image2.jpg'],
  }

  const handleSubmit = async (data: CreateClubCommand) => {
    console.log('Updated club data:', data)
    // TODO: Implement club update logic
    navigate('/club')
  }

  const handleCancel = () => {
    navigate('/club')
  }

  return (
    <ClubForm
      mode="edit"
      initialData={mockClubData}
      onSubmit={handleSubmit}
      onCancel={handleCancel}
    />
  )
}

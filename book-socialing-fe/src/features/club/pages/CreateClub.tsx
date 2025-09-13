import { useNavigate } from 'react-router-dom'

import type { CreateClubCommand } from '@/types/club'

import ClubForm from '../components/ClubForm'

export default function CreateClub() {
  const navigate = useNavigate()

  const handleSubmit = async (data: CreateClubCommand) => {
    console.log('Club data:', data)
    // TODO: Implement club creation logic
    navigate('/club')
  }

  return <ClubForm mode="create" onSubmit={handleSubmit} />
}

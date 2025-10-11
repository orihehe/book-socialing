import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { User } from '@/types/user'

interface Prop {
  user: User
  onClick?: (user: User) => void
  size?: number
}

export function UserImage({ user, onClick, size = 10 }: Prop) {
  return (
    <Avatar
      key={user.id}
      className={`w-${size} h-${size} shadow-sm ${onClick && 'cursor-point'}`}
      onClick={() => onClick?.(user)}
    >
      <AvatarImage src={/* getUserImage(p.userId) */ undefined} />
      <AvatarFallback className="text-xs bg-gray-400 text-white">{user.nickname[0]}</AvatarFallback>
    </Avatar>
  )
}

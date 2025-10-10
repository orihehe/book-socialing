import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { User } from '@/types/user'

interface Prop {
  user: User
}

export function UserImage({ user }: Prop) {
  return (
    <Avatar key={user.id} className="w-10 h-10 border border-white shadow-sm">
      <AvatarImage src={/* getUserImage(p.userId) */ undefined} />
      <AvatarFallback className="text-xs bg-red-400 text-white">{user.nickname[0]}</AvatarFallback>
    </Avatar>
  )
}

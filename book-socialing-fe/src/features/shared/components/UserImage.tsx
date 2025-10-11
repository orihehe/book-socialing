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
      className={`w-${size} h-${size} shadow-sm`}
      onClick={() => onClick?.(user)}
    >
      <AvatarImage
        className={`${onClick ? 'cursor-pointer' : ''}`}
        src={undefined /* 실제 이미지 URL이 있으면 여기에 할당 */}
        alt={user.nickname}
      />
      <AvatarFallback className={`bg-gray-400 text-white text-[${size}px]`}>
        {user.nickname?.[0] ?? '?'}
      </AvatarFallback>
    </Avatar>
  )
}

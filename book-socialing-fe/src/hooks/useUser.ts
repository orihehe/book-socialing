import { useQuery } from '@tanstack/react-query'

import { apiFetch } from '@/lib/api'
import { User } from '@/types/user'

export function useUser() {
  const hasToken = !!localStorage.getItem('accessToken')

  const query = useQuery<User>({
    queryKey: ['user', 'me'],
    queryFn: async () => {
      const response = await apiFetch('/v1/user/me')
      if (!response.ok) {
        throw new Error('Failed to fetch user info')
      }
      return response.json()
    },
    enabled: hasToken,
    retry: 1,
    staleTime: Infinity, // localStorage처럼 무한대로 캐시 유지
    gcTime: Infinity, // 가비지 컬렉션도 무한대로 설정
  })

  return {
    user: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    hasToken,
  }
}

export async function apiFetch(path: string, options: RequestInit = {}) {
  const accessToken = localStorage.getItem('accessToken')
  const headers = new Headers(options.headers)

  headers.set('Authorization', `Bearer ${accessToken}`)

  const res = await fetch(`/api${path}`, {
    ...options,
    headers,
  })

  if (res.status === 401) {
    window.location.href = '/sign-in'
    return Promise.reject(new Error('Unauthorized - redirect to login'))
  }

  // 400 이상이면 에러 (200-399는 성공으로 처리)
  if (res.status >= 400) {
    throw new Error(`HTTP error! status: ${res.status}`)
  }

  return res
}

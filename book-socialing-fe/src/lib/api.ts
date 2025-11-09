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

  if (!res.ok) {
    throw new Error(`HTTP error! status: ${res.status}`)
  }

  return res
}

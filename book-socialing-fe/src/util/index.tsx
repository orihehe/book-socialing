const API_URL = import.meta.env.VITE_API_BASE_URL

export function getImageUrl(imagePath: string = '') {
  // 윈도우 환경에서 경로에 \ 가 포함되어 있을 경우, /로 변환하여 사용
  imagePath = imagePath?.replace(/\\/g, '/')

  if (imagePath.startsWith('http')) {
    return imagePath // 이미 전체 URL인 경우
  }

  if (!imagePath) {
    return `${API_URL}/images/default_book_image.jpg`
  }

  // filePath를 URL 인코딩하여 특수문자 처리
  return `${API_URL}/api/v1/file?filePath=${encodeURIComponent(imagePath)}`
}

export async function getImageFile(imageUrl?: string) {
  let bookImages: File[] = []
  if (imageUrl) {
    try {
      const imagePath = getImageUrl(imageUrl)
      const imgResponse = await fetch(imagePath)

      if (imgResponse.ok) {
        const blob = await imgResponse.blob()
        const file = new File([blob], 'book-image.jpg', { type: blob.type })
        bookImages = [file]
      }
    } catch {
      bookImages = []
    }
  }

  return bookImages
}

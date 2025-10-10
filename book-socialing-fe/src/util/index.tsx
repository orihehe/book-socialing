export function getImageUrl(imagePath: string = '/images/default_book_image.jpg') {
  if (imagePath.startsWith('http')) {
    return imagePath // 이미 전체 URL인 경우
  }

  // 윈도우 환경에서 경로에 \ 가 포함되어 있을 경우, /로 변환하여 사용
  imagePath = imagePath.replace(/\\/g, '/')

  return `http://localhost:8080/api/v1/file?filePath=${imagePath}`
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

export function getImageUrl(imagePath: string = '/images/default_book_image.jpg') {
  if (imagePath.startsWith('http')) {
    return imagePath // 이미 전체 URL인 경우
  }

  const tmpImage = '/images/default_book_image.jpg'
  return `http://localhost:8080${tmpImage}`
}

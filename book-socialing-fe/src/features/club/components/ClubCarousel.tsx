import { useState, useEffect } from 'react'

import { Carousel, CarouselContent, CarouselItem, type CarouselApi } from '@/components/ui/carousel'
import { cn } from '@/lib/utils'
import { getImageUrl } from '@/util'

interface Props {
  images: string[]
}

export function ClubCarousel({ images }: Props) {
  const [api, setApi] = useState<CarouselApi>()
  const [current, setCurrent] = useState(0)

  useEffect(() => {
    if (!api) {
      return
    }

    setCurrent(api.selectedScrollSnap() + 1)
    api.on('select', () => {
      setCurrent(api.selectedScrollSnap() + 1)
    })
  }, [api])

  return (
    <div className="mb-3">
      <Carousel setApi={setApi}>
        <CarouselContent>
          {images.length > 0 ? (
            images.map(url => (
              <div className="min-w-full" key={url}>
                <CarouselItem>
                  <div className="flex justify-center items-center w-full min-h-64">
                    <img
                      src={getImageUrl(url)}
                      className="mx-auto"
                      onError={e => {
                        const target = e.target as HTMLImageElement
                        target.style.display = 'none'
                        const parent = target.parentElement
                        if (parent) {
                          parent.classList.add('bg-gray-100', 'h-64')
                          const placeholder = document.createElement('p')
                          placeholder.className = 'text-gray-400 text-sm'
                          placeholder.textContent = '이미지를 불러올 수 없습니다'
                          parent.appendChild(placeholder)
                        }
                      }}
                    />
                  </div>
                </CarouselItem>
              </div>
            ))
          ) : (
            <CarouselItem>
              <div className="flex justify-center items-center w-full h-64 bg-gray-100">
                <p className="text-gray-400 text-sm">이미지가 없습니다</p>
              </div>
            </CarouselItem>
          )}
        </CarouselContent>
      </Carousel>
      {images.length > 1 && (
        <div
          role="tablist"
          className="w-full flex items-center justify-center gap-2"
          style={{
            position: 'relative',
            top: '-18px', // 이미지와 겹치도록 위로 겹치게 올림
            zIndex: 2,
          }}
        >
          {api?.scrollSnapList().map((_, index) => (
            <button
              key={index}
              role="tab"
              data-slot="carousel-dot"
              aria-selected={index === current - 1}
              aria-controls="carousel-item"
              aria-label={`Slide ${index + 1}`}
              className={cn(
                'w-2 h-2 rounded-full transition-all duration-200 cursor-pointer',
                index === current - 1
                  ? 'bg-main scale-110 shadow w-3'
                  : 'bg-gray-200 hover:bg-gray-300'
              )}
              style={{
                margin: '0 2px',
              }}
              onClick={() => api?.scrollTo(index)}
            />
          ))}
        </div>
      )}
    </div>
  )
}

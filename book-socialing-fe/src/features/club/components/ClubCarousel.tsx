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
          {images.map(url => (
            <div className="min-w-full" key={url}>
              <CarouselItem>
                <div className="flex justify-center items-center w-full h-full">
                  <img src={getImageUrl(url)} className="mx-auto" />
                </div>
              </CarouselItem>
            </div>
          ))}
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

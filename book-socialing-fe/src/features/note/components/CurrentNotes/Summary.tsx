import { useState, useEffect } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { CardContent } from '@/components/ui/card'
import { Carousel, CarouselContent, CarouselItem, type CarouselApi } from '@/components/ui/carousel'
import { cn } from '@/lib/utils'
import type { Note } from '@/types/note'

import { CurrentNoteCard } from './CurrentNoteCard'

interface Props {
  currentNotes: Note[]
}

export function Summary({ currentNotes }: Props) {
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
    <BaseCard title={`📖 열린 노트 (${currentNotes.length})`}>
      <CardContent>
        <Carousel setApi={setApi}>
          <CarouselContent>
            {currentNotes.map(({ id, title, author, description, endDateTime, imageUrl }) => (
              <div className="min-w-full" key={id}>
                <CarouselItem>
                  <CurrentNoteCard
                    title={title}
                    author={author}
                    imageUrl={imageUrl}
                    description={description}
                    endDateTime={endDateTime}
                  />
                </CarouselItem>
              </div>
            ))}
          </CarouselContent>
          {currentNotes.length > 1 && (
            <div
              role="tablist"
              className="bottom-0 w-full flex items-center justify-center gap-2 mt-3"
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
                    'w-5 h-2 rounded-full transition-all duration-200 cursor-pointer',
                    index === current - 1
                      ? 'bg-main scale-110 shadow'
                      : 'bg-gray-200 hover:bg-gray-300'
                  )}
                  style={{
                    margin: '0 4px',
                  }}
                  onClick={() => api?.scrollTo(index)}
                />
              ))}
            </div>
          )}
        </Carousel>
      </CardContent>
    </BaseCard>
  )
}

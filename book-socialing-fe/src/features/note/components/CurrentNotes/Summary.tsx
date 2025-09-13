import { ChevronRight } from 'lucide-react'
import { useState, useEffect } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import { Carousel, CarouselContent, CarouselItem, type CarouselApi } from '@/components/ui/carousel'
import { cn } from '@/lib/utils'
import type { Note } from '@/types/note'

import { CurrentNoteCard } from './CurrentNoteCard'

interface Props {
  currentNotes: Note[]
  moveToAll: () => void
}

export function Summary({ currentNotes, moveToAll }: Props) {
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
    <div>
      <BaseCard title={`열린 노트 (${currentNotes.length})`}>
        <CardContent>
          <Carousel setApi={setApi}>
            <CarouselContent>
              {currentNotes.map(({ id, title, author, description, endDateTime, imageUrl }) => (
                <div className="min-w-full" key={id}>
                  <CarouselItem>
                    <CurrentNoteCard
                      id={id}
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
          </Carousel>
        </CardContent>
      </BaseCard>
      {currentNotes.length > 1 && (
        <div role="tablist" className="bottom-0 w-full flex items-center justify-center gap-2 mt-3">
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
      <div className="px-4 mt-4">
        <Button
          variant="ghost"
          className="w-full justify-center items-center gap-1 text-muted-foreground text-sm px-6 py-6 rounded-5 border"
          style={{
            borderColor: '#E7ECEC',
            color: '#7D7D7D',
          }}
          onClick={moveToAll}
        >
          열린 노트 전체보기 <ChevronRight />
        </Button>
      </div>
    </div>
  )
}

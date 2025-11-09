import { useQuery } from '@tanstack/react-query'
import { ChevronRight } from 'lucide-react'
import { useState, useEffect } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import { Carousel, CarouselContent, CarouselItem, type CarouselApi } from '@/components/ui/carousel'
import { LoadingPage } from '@/features/shared/components/LoadingPage'
import { apiFetch } from '@/lib/api'
import { cn } from '@/lib/utils'
import type { ClubNotesPageResponse } from '@/types/note'

import { CurrentNoteCard } from './CurrentNoteCard'

interface Props {
  moveToAll: () => void
}

export function Summary({ moveToAll }: Props) {
  const [api, setApi] = useState<CarouselApi>()
  const [current, setCurrent] = useState(0)

  const { data: currentNotesData, isLoading } = useQuery({
    queryKey: ['openNotes'],
    queryFn: async (): Promise<ClubNotesPageResponse> => {
      const response = await apiFetch('/v1/note/open', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('Failed to fetch open notes')
      }

      const data = await response.json()

      return data
    },
  })

  const currentNotes = currentNotesData?.groups.flatMap(({ notes }) => notes) || []
  useEffect(() => {
    if (!api) {
      return
    }

    setCurrent(api.selectedScrollSnap() + 1)
    api.on('select', () => {
      setCurrent(api.selectedScrollSnap() + 1)
    })
  }, [api])

  if (isLoading) return <LoadingPage className="h-30" />

  return (
    <div>
      <BaseCard title={`열린 노트 (${currentNotes.length})`}>
        <CardContent>
          <Carousel setApi={setApi}>
            <CarouselContent>
              {currentNotes.length === 0 ? (
                <div className="min-w-full">
                  <CarouselItem>
                    <div className="flex flex-col items-center justify-center py-12 text-center">
                      <div className="text-gray-500 text-lg font-medium mb-2">
                        나의 열린 노트가 없습니다.
                      </div>
                      <div className="text-gray-400 text-sm">노트에 참여해 보세요!</div>
                    </div>
                  </CarouselItem>
                </div>
              ) : (
                currentNotes.map(
                  ({
                    id,
                    participants,
                    bookName,
                    bookAuthor,
                    description,
                    endAt,
                    bookImageUrl,
                  }) => (
                    <div className="min-w-full" key={id}>
                      <CarouselItem>
                        <CurrentNoteCard
                          id={id}
                          participants={participants}
                          bookName={bookName}
                          bookAuthor={bookAuthor}
                          bookImageUrl={bookImageUrl}
                          description={description}
                          endAt={endAt}
                        />
                      </CarouselItem>
                    </div>
                  )
                )
              )}
            </CarouselContent>
          </Carousel>
        </CardContent>
      </BaseCard>
      {currentNotes.length > 1 && (
        <div role="tablist" className="bottom-0 w-full flex items-center justify-center gap-2 mt-0">
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
      {!!currentNotes.length && (
        <div className="px-4 my-4">
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
      )}
    </div>
  )
}

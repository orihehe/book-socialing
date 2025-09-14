import { BaseButton } from '@/components/common/BaseButton'
import { BaseCard } from '@/components/common/BaseCard'
import { CardContent } from '@/components/ui/card'
import type { Note } from '@/types/note'

import { DefaultNote } from './DefaultNote'

export function SuggestedNotes() {
  const suggestedNotes: Note[] = []

  return suggestedNotes.length ? (
    <BaseCard title={`추천 노트 (${suggestedNotes.length})`}>
      <CardContent>
        {suggestedNotes.map(note => (
          <DefaultNote key={note.id} {...note}>
            <BaseButton className="flex-shrink-0">신청</BaseButton>
          </DefaultNote>
        ))}
      </CardContent>
    </BaseCard>
  ) : null
}

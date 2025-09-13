import { BaseButton } from '@/components/common/BaseButton'
import type { Note } from '@/types/note'

import { DefaultNote } from '../shared/DefaultNote'

type MyNoteCardProps = Omit<Note, 'id'>

export function CreatedNote(props: MyNoteCardProps) {
  return (
    <DefaultNote {...props}>
      <BaseButton className="flex-shrink-0">수정</BaseButton>
      <BaseButton className="flex-shrink-0">관리</BaseButton>
    </DefaultNote>
  )
}

export function AppliedNote(props: MyNoteCardProps) {
  return (
    <DefaultNote {...props}>
      <BaseButton className="flex-shrink-0">신청취소</BaseButton>
    </DefaultNote>
  )
}

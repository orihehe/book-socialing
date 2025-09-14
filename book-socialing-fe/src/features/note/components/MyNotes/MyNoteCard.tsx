import { useNavigate } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import type { ClubNotesGroup, Note } from '@/types/note'

import { DefaultNote } from '../shared/DefaultNote'

export function CreatedNote({ notes, ...club }: ClubNotesGroup) {
  const navigate = useNavigate()
  return (
    <div className="">
      <div>{club.clubName}</div>
      {notes.map(note => (
        <DefaultNote {...note}>
          <BaseButton className="flex-shrink-0" onClick={() => navigate(`/note/${props.id}/edit`)}>
            수정
          </BaseButton>
          <BaseButton className="flex-shrink-0" onClick={() => navigate(`/note/${props.id}/guest`)}>
            관리
          </BaseButton>
        </DefaultNote>
      ))}
    </div>
  )
}

export function AppliedNote(props: Note) {
  return (
    <DefaultNote {...props}>
      <BaseButton className="flex-shrink-0">신청취소</BaseButton>
    </DefaultNote>
  )
}

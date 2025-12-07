import { useNavigate } from 'react-router-dom'

import { BaseButton } from '@/components/common/BaseButton'
import type { ClubNotesGroup } from '@/types/note'

import { DefaultNote } from '../shared/DefaultNote'

export function CreatedNote({ notes, ...club }: ClubNotesGroup) {
  const navigate = useNavigate()
  return (
    <div>
      {club.id && <div>{club.clubName}</div>}
      {notes.map(note => (
        <DefaultNote key={note.id} {...note}>
          <BaseButton className="flex-shrink-0" onClick={() => navigate(`/note/${note.id}/edit`)}>
            수정
          </BaseButton>
          <BaseButton className="flex-shrink-0" onClick={() => navigate(`/note/${note.id}/guest`)}>
            관리
          </BaseButton>
        </DefaultNote>
      ))}
    </div>
  )
}

export function AppliedNote({ notes, ...club }: ClubNotesGroup) {
  return (
    <div className="">
      {club.clubName && <div>{club.clubName}</div>}
      {notes.map(note => (
        <DefaultNote key={note.id} {...note}>
          <BaseButton className="flex-shrink-0">신청취소</BaseButton>
        </DefaultNote>
      ))}
    </div>
  )
}

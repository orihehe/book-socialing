import { motion, AnimatePresence } from 'framer-motion'
import { useState } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import type { Note } from '@/types/note'

import { MyNoteCard } from './MyNoteCard'

interface MyNotesProps {
  myNotes: Note[]
}

const VISIBLE_NOTE_COUNT = 2

export function MyNotes({ myNotes }: MyNotesProps) {
  const [open, setOpen] = useState(false)
  const showToggle = myNotes.length > VISIBLE_NOTE_COUNT
  const visibleNotes = !showToggle || open ? myNotes : [...myNotes].splice(0, VISIBLE_NOTE_COUNT)

  return (
    <BaseCard title={`내가 생성한 노트 (${myNotes.length})`}>
      <CardContent>
        <div className="flex flex-col gap-2">
          {visibleNotes.slice(0, VISIBLE_NOTE_COUNT).map(({ id, ...note }) => (
            <MyNoteCard key={id} {...note} />
          ))}
        </div>
        <AnimatePresence initial={false}>
          {open && visibleNotes.length > 2 && (
            <motion.div
              key="extra-note-list"
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.3 }}
              className="overflow-hidden"
            >
              <div className="flex flex-col gap-2 mt-2">
                {visibleNotes.slice(2).map(({ id, ...note }) => (
                  <MyNoteCard key={id} {...note} />
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>

        {showToggle && (
          <Button
            variant="ghost"
            className="w-full mt-2 text-gray-400"
            onClick={() => setOpen(o => !o)}
          >
            {open ? (
              <>
                생성한 노트 접기{' '}
                <span className="inline-block align-middle">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path
                      d="M4 10L8 6L12 10"
                      stroke="currentColor"
                      strokeWidth="1.5"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                </span>
              </>
            ) : (
              <>
                생성한 노트 펼치기{' '}
                <span className="inline-block align-middle">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path
                      d="M4 6L8 10L12 6"
                      stroke="currentColor"
                      strokeWidth="1.5"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                </span>
              </>
            )}
          </Button>
        )}
      </CardContent>
    </BaseCard>
  )
}

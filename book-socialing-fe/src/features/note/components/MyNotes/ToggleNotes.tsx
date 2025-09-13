import { motion, AnimatePresence } from 'framer-motion'
import { useState } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import type { Note } from '@/types/note'

interface MyNotesProps {
  title: string
  myNotes: Note[]
  NoteComponent: React.ComponentType<Note>
}

export function ToggleNotes({ title, myNotes, NoteComponent }: MyNotesProps) {
  const [open, setOpen] = useState(true)

  return (
    <BaseCard title={`${title} (${myNotes.length})`}>
      <CardContent>
        <AnimatePresence initial={true}>
          {open && myNotes.length > 2 && (
            <motion.div
              key="extra-note-list"
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.3 }}
              className="overflow-hidden mb-2"
            >
              <div className="flex flex-col gap-2 mt-2">
                {myNotes.map(note => (
                  <NoteComponent key={note.id} {...note} />
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>

        <Button variant="ghost" className="w-full text-gray-400" onClick={() => setOpen(o => !o)}>
          {open ? (
            <>
              {title} 접기{' '}
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
              {title} 펼치기{' '}
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
      </CardContent>
    </BaseCard>
  )
}

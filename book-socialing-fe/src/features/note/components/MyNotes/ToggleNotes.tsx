import { motion, AnimatePresence } from 'framer-motion'
import { useState } from 'react'

import { BaseCard } from '@/components/common/BaseCard'
import { Button } from '@/components/ui/button'
import { CardContent } from '@/components/ui/card'
import type { ClubNotesGroup, ClubNotesPageResponse } from '@/types/note'

interface MyNotesProps {
  title: string
  result?: ClubNotesPageResponse
  NoteComponent: React.ComponentType<ClubNotesGroup>
}

export function ToggleNotes({ title, result, NoteComponent }: MyNotesProps) {
  const [open, setOpen] = useState(true)
  const { groups, totalCount } = result ?? { groups: [], totalCount: 0 }

  if (!groups.length) {
    return null
  }

  return (
    <BaseCard title={`${title} (${totalCount})`}>
      <CardContent>
        <AnimatePresence initial={true}>
          {open && (
            <motion.div
              key="extra-note-list"
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.3 }}
              className="overflow-hidden mb-2"
            >
              <div className="flex flex-col gap-2 mt-2">
                {groups.map(group => (
                  <NoteComponent key={group.id} {...group} />
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

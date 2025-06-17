import type { Meta, StoryObj } from '@storybook/react-vite'
import { CurrentNoteCard } from './CurrentNoteCard'

const meta: Meta<typeof CurrentNoteCard> = {
  title: 'features/note/CurrentNoteCard',
  component: CurrentNoteCard,
}

export default meta
type Story = StoryObj<typeof CurrentNoteCard>

export const Default: Story = {
  args: {
    title: '두 개의 탑',
    author: 'JRR Tolkein',
    likedCount: 2,
    isCompleted: false,
    imageUrl: 'https://placehold.co/600x400',
    description: '설명입니다.',
    dDay: 4,
  },
}

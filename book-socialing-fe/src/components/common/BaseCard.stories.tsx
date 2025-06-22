import { BaseCard } from './BaseCard'

import type { Meta, StoryObj } from '@storybook/react-vite'

const meta: Meta<typeof BaseCard> = {
  title: 'common/BaseCard',
  component: BaseCard,
}

export default meta
type Story = StoryObj<typeof BaseCard>

export const Default: Story = {
  args: {
    title: '기본 카드 제목',
  },
}

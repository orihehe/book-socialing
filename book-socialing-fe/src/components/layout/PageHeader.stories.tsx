import { MemoryRouter } from 'react-router-dom'

import { Button } from '@/components/ui/button'

import { PageHeader } from './PageHeader'

import type { Meta, StoryObj } from '@storybook/react-vite'

const meta: Meta<typeof PageHeader> = {
  title: 'Layout/PageHeader',
  component: PageHeader,
  tags: ['autodocs'],
  parameters: {
    layout: 'centered',
  },
  decorators: [
    Story => (
      <MemoryRouter>
        <Story />
      </MemoryRouter>
    ),
  ],
}

export default meta

type Story = StoryObj<typeof PageHeader>

export const Default: Story = {
  args: {
    title: '페이지 타이틀',
    showBack: true,
    children: undefined,
  },
}

export const TitleOnly: Story = {
  args: {
    title: '타이틀만',
    showBack: false,
    children: undefined,
  },
}

export const WithRightSlot: Story = {
  args: {
    title: '오른쪽 슬롯',
    showBack: true,
    children: <Button>액션</Button>,
  },
}

export const NoBackButton: Story = {
  args: {
    title: '뒤로가기 없음',
    showBack: false,
    children: <Button>설정</Button>,
  },
}

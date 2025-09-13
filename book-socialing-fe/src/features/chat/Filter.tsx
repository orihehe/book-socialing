import { BaseButton } from '@/components/common/BaseButton'

import { FILTER_TYPE, FilterType } from './const'

type FilterProps = {
  activeFilter?: FilterType
  setActiveFilter: (filter: FilterType) => void
}

export function Filter({ activeFilter, setActiveFilter }: FilterProps) {
  return (
    <div className="flex gap-2">
      <BaseButton
        isActive={activeFilter === FILTER_TYPE.NOTICE}
        onClick={() => setActiveFilter(FILTER_TYPE.NOTICE)}
      >
        공지
      </BaseButton>
      <BaseButton
        isActive={activeFilter === FILTER_TYPE.QUESTION}
        onClick={() => setActiveFilter(FILTER_TYPE.QUESTION)}
      >
        질문
      </BaseButton>
      <BaseButton
        isActive={activeFilter === FILTER_TYPE.REVIEW}
        onClick={() => setActiveFilter(FILTER_TYPE.REVIEW)}
      >
        감상
      </BaseButton>
      <BaseButton
        isActive={activeFilter === FILTER_TYPE.GENERAL}
        onClick={() => setActiveFilter(FILTER_TYPE.GENERAL)}
      >
        자유
      </BaseButton>
      <BaseButton
        isActive={activeFilter === FILTER_TYPE.My}
        onClick={() => setActiveFilter(FILTER_TYPE.My)}
      >
        내글만보기
      </BaseButton>
    </div>
  )
}

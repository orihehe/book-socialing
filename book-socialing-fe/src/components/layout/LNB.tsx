import React from 'react'
import { Link, useLocation } from 'react-router-dom'

export interface LNBItem {
  name: string
  key: string
  children?: LNBItem[]
}

interface LNBProps {
  items: LNBItem[]
  activeTab?: string
  onTabChange?: (key: string) => void
}

const MARGIN_LEFT = 16
const TAB_WIDTH = 41

export default function LNB({ items, activeTab, onTabChange }: LNBProps) {
  const { pathname } = useLocation()

  const activeFirstLevel = pathname.slice(1)

  const activeItemIndex = items.findIndex(item => item.key === activeFirstLevel)
  const activeItem = activeItemIndex !== -1 ? items[activeItemIndex] : items[0]

  const hasChildren = activeItem?.children && activeItem.children.length > 0

  return (
    <div className="w-full">
      {/* First Level Navigation */}
      <div className="border-b border-gray-200">
        <div className="flex space-x-8 px-6 pt-4">
          {items.map(item => (
            <Link
              key={item.key}
              to={`/${item.key}`}
              className={`text-sm font-medium transition-colors ${
                activeFirstLevel === item.key
                  ? 'text-gray-900 border-b-2 border-gray-900 pb-1'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              {item.name}
            </Link>
          ))}
        </div>
      </div>

      {/* Second Level Navigation - children이 있을 때만 표시 */}
      {hasChildren && (
        <div className="border-none relative">
          <div
            className="flex space-x-8 px-6 py-4"
            style={{ marginLeft: `${activeItemIndex * TAB_WIDTH + MARGIN_LEFT}px` }}
          >
            {activeItem!.children!.map(child => (
              <button
                key={child.key}
                onClick={() => onTabChange?.(child.key)}
                className={`text-sm font-medium transition-colors ${
                  activeTab === child.key
                    ? 'text-gray-900 font-semibold'
                    : 'text-gray-500 hover:text-gray-700'
                }`}
              >
                {child.name}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

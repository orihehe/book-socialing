import { Link } from 'react-router-dom'

export interface LNBItem {
  name: string
  key: string
  children?: LNBItem[]
}

interface LNBProps {
  items: LNBItem[]
  activeChildIndex?: number
  activeItemIndex: number
  onChildTabChange?: (index: number) => void
  getRoutePath?: (key: string) => string
}

const MARGIN_LEFT = 16
const TAB_WIDTH = 41

export default function LNB({
  items,
  activeChildIndex = 0,
  onChildTabChange,
  activeItemIndex,
  getRoutePath = key => `/${key}`,
}: LNBProps) {
  const activeItem = activeItemIndex !== -1 ? items[activeItemIndex] : items[0]

  const hasChildren = activeItem?.children && activeItem.children.length > 0

  return (
    <div className="w-full">
      {/* First Level Navigation */}
      <div className="border-b border-gray-200">
        <div className="flex space-x-8 px-6 pt-4">
          {items.map((item, index) => (
            <Link
              key={item.key}
              to={getRoutePath(item.key)}
              className={`text-sm font-semibold transition-colors ${
                activeItemIndex === index
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
            {activeItem!.children!.map((child, index) => (
              <button
                key={child.key}
                onClick={() => onChildTabChange?.(index)}
                className={`text-sm transition-colors font-semibold ${
                  activeChildIndex === index ? 'text-gray-900' : 'text-gray-500 hover:text-gray-700'
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

interface BottomButtonProps {
  onClick: () => void
  children: React.ReactNode
}

export function BottomButton({ onClick, children }: BottomButtonProps) {
  return (
    <>
      <div className="fixed bottom-6 left-4 right-4">
        <button
          type="submit"
          onClick={onClick}
          className="w-full bg-main hover:bg-main/90 text-white py-4 text-base font-medium rounded-lg"
        >
          {children}
        </button>
      </div>

      {/* 하단 여백 - 고정 버튼 높이만큼 */}
      <div className="h-24"></div>
    </>
  )
}

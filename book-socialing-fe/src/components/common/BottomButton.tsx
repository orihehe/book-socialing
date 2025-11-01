interface BottomButtonProps {
  onClick?: () => void
  children: React.ReactNode
  type?: 'submit' | 'button'
  disabled?: boolean
  isSub?: boolean
}

export function BottomButton({
  onClick,
  children,
  isSub,
  type = 'submit',
  disabled,
}: BottomButtonProps) {
  return (
    <>
      <div
        className={`fixed left-0 right-0 px-4 max-w-[500px] mx-auto ${isSub ? 'bottom-20 mb-2' : 'bottom-6'}`}
      >
        <button
          type={type}
          onClick={onClick}
          disabled={disabled}
          className={`w-full py-4 text-base font-medium rounded-lg 
            ${
              isSub
                ? 'bg-[#F2F4F6] text-main hover:bg-[#F2F4F6]/90'
                : 'bg-main text-white hover:bg-main/90'
            }`}
        >
          {children}
        </button>
      </div>

      {/* 하단 여백 - 고정 버튼 높이만큼 */}
      <div className="h-24" />
    </>
  )
}

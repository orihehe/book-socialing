import { Link } from 'react-router-dom'

export default function NotFound() {
  return (
    <main className="min-h-[60vh] grid place-items-center p-8 text-center">
      <div>
        <h1 className="text-2xl font-bold">페이지를 찾을 수 없어요 (404)</h1>
        <p className="mt-2 text-muted-foreground">주소가 바뀌었거나 삭제되었을 수 있어요.</p>
        <Link
          to="/"
          className="inline-block mt-6 px-4 py-2 rounded-md bg-emerald-700 text-white hover:bg-emerald-600"
        >
          홈으로 가기
        </Link>
      </div>
    </main>
  )
}

import { Card, CardHeader, CardTitle, CardContent, CardFooter } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'

interface Props {
  title: string
  author: string
  likedCount: number
  isCompleted: boolean
  imageUrl: string
  description?: string
  dDay?: number
}

export function CurrentNoteCard({ title, author, likedCount, imageUrl, description, dDay }: Props) {
  return (
    <Card className="w-[300px]">
      <CardHeader>
        <CardTitle className="text-base font-semibold">작성중인 노트</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex gap-4">
          <img src={imageUrl} alt={title} className="w-24 h-32 object-cover rounded-md border" />
          <div className="flex flex-col justify-between flex-1">
            <div>
              <div className="font-bold text-lg">{title}</div>
              <div className="text-sm text-muted-foreground">{author}</div>
              <div className="text-xs text-gray-400 mt-1">{description}</div>
            </div>
            <div className="flex items-center gap-2 mt-2">
              <Badge variant="secondary" className="px-2 py-0.5 text-xs">
                {dDay}
              </Badge>
              <span className="flex items-center gap-1 text-sm">
                <span className="w-2 h-2 rounded-full bg-red-400 inline-block" />+{likedCount}
              </span>
            </div>
          </div>
        </div>
      </CardContent>
      <CardFooter>
        <Button className="w-full" variant="default">
          노트하러가기
        </Button>
      </CardFooter>
    </Card>
  )
}

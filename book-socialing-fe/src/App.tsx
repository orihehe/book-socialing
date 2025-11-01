// App.tsx
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Routes, Route, Navigate } from 'react-router-dom'
import { Toaster } from 'sonner'

import ChatView from './features/chat/ChatView'
import ClubView from './features/club/ClubView'
import ClubDetail from './features/club/pages/ClubDetail'
import CreateClub from './features/club/pages/CreateClub'
import EditClub from './features/club/pages/EditClub'
import MemberManagement from './features/club/pages/MemberManagement'
import NoteView from './features/note/NoteView'
import CreateNote from './features/note/pages/CreateNote'
import EditNote from './features/note/pages/EditNote'
import GuestManagement from './features/note/pages/GuestManagement'
import SearchView from './features/search/SearchView'
import KakaoCallback from './features/user/KakaoCallback'
import My from './features/user/My'
import MyEdit from './features/user/MyEdit'
import SignIn from './features/user/SignIn'
import NotFound from './pages/NotFound'

const queryClient = new QueryClient()

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Toaster />

      <Routes>
        <Route path="/" element={<Navigate to="/note" replace />} />

        {/* note */}
        <Route path="/note" element={<NoteView />} />
        <Route path="/note/create" element={<CreateNote />} />
        <Route path="/note/:id" element={<ChatView />} />
        <Route path="/note/:id/edit" element={<EditNote />} />
        <Route path="/note/:id/guest" element={<GuestManagement />} />

        {/* club */}
        <Route path="/club" element={<ClubView />} />
        <Route path="/club/create" element={<CreateClub />} />
        <Route path="/club/:id" element={<ClubDetail />} />
        <Route path="/club/:id/edit" element={<EditClub />} />
        <Route path="/club/:id/members" element={<MemberManagement />} />
        {/* <Route path="/about" element={<AboutPage />} /> */}

        {/* user */}
        <Route path="/my" element={<My />} />
        <Route path="/my/edit" element={<MyEdit />} />
        <Route path="/sign-in" element={<SignIn />} />
        <Route path="/oauth/callback" element={<KakaoCallback />} />

        {/* etc */}
        <Route path="/search" element={<SearchView />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </QueryClientProvider>
  )
}

export default App

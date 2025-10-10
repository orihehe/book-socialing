// App.tsx
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Routes, Route, Navigate } from 'react-router-dom'

import ChatView from './features/chat/ChatView'
import ClubView from './features/club/ClubView'
import ClubDetail from './features/club/pages/ClubDetail'
import CreateClub from './features/club/pages/CreateClub'
import EditClub from './features/club/pages/EditClub'
import CreateNote from './features/note/pages/CreateNote'
import EditNote from './features/note/pages/EditNote'
import GuestManagementPage from './features/note/pages/GuestManagement'
import KakaoCallback from './features/user/KakaoCallback'
import SignIn from './features/user/SignIn'
import NoteView from './pages/note'

const queryClient = new QueryClient()

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Routes>
        <Route path="/" element={<Navigate to="/note" replace />} />

        {/* note */}
        <Route path="/note" element={<NoteView />} />
        <Route path="/note/create" element={<CreateNote />} />
        <Route path="/note/:id" element={<ChatView />} />
        <Route path="/note/:id/edit" element={<EditNote />} />
        <Route path="/note/:id/guest" element={<GuestManagementPage />} />

        {/* club */}
        <Route path="/club" element={<ClubView />} />
        <Route path="/club/create" element={<CreateClub />} />
        <Route path="/club/:id" element={<ClubDetail />} />
        <Route path="/club/:id/edit" element={<EditClub />} />
        {/* <Route path="/about" element={<AboutPage />} /> */}

        {/* user */}
        <Route path="/sign-in" element={<SignIn />} />
        <Route path="/auth/kakao/callback" element={<KakaoCallback />} />
      </Routes>
    </QueryClientProvider>
  )
}

export default App

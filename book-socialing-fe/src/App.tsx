// App.tsx
import { Routes, Route, Navigate } from 'react-router-dom'

import ChatView from './features/chat/ChatView'
import ClubView from './features/club/ClubView'
import CreateClub from './features/club/pages/CreateClub'
import EditClub from './features/club/pages/EditClub'
import EditNote from './features/note/pages/EditNote'
import GuestManagementPage from './features/note/pages/GuestManagement'
import NoteView from './pages/note'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/note" replace />} />
      <Route path="/note" element={<NoteView />} />
      <Route path="/note/create" element={<EditNote mode="create" />} />
      <Route path="/note/:id" element={<ChatView />} />
      <Route path="/note/:id/edit" element={<EditNote mode="edit" />} />
      <Route path="/note/:id/guest" element={<GuestManagementPage />} />
      <Route path="/club" element={<ClubView />} />
      <Route path="/club/create" element={<CreateClub />} />
      <Route path="/club/:id/edit" element={<EditClub />} />
      {/* <Route path="/about" element={<AboutPage />} /> */}
    </Routes>
  )
}

export default App

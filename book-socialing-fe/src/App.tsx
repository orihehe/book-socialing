// App.tsx
import { Routes, Route, Navigate } from 'react-router-dom'

import ClubView from './features/club/ClubView'
import CreateClub from './features/club/pages/CreateClub'
import EditClub from './features/club/pages/EditClub'
import CreateNote from './features/note/pages/CreateNote'
import NoteView from './pages/note'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/note" replace />} />
      <Route path="/note" element={<NoteView />} />
      <Route path="/note/create" element={<CreateNote />} />
      <Route path="/club" element={<ClubView />} />
      <Route path="/club/create" element={<CreateClub />} />
      <Route path="/club/:id/edit" element={<EditClub />} />
      {/* <Route path="/about" element={<AboutPage />} /> */}
    </Routes>
  )
}

export default App

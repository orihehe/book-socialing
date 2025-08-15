// App.tsx
import { Routes, Route, Navigate } from 'react-router-dom'

import ClubView from './features/club/ClubView'
import NoteView from './pages/note'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/note" replace />} />
      <Route path="/note" element={<NoteView />} />
      <Route path="/club" element={<ClubView />} />
      {/* <Route path="/about" element={<AboutPage />} /> */}
    </Routes>
  )
}

export default App

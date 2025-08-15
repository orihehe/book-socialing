// App.tsx
import { Routes, Route, Navigate } from 'react-router-dom'

import NoteView from './pages/note'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/note" replace />} />
      <Route path="/note" element={<NoteView />} />
      <Route path="/club" element={<NoteView />} />
      {/* <Route path="/about" element={<AboutPage />} /> */}
    </Routes>
  )
}

export default App

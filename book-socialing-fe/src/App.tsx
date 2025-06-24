// App.tsx
import { Routes, Route } from 'react-router-dom'

import NoteView from './pages/note'

function App() {
  return (
    <Routes>
      <Route path="/" element={<NoteView />} />
      {/* <Route path="/about" element={<AboutPage />} /> */}
    </Routes>
  )
}

export default App

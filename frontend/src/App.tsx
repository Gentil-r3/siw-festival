import { BrowserRouter, Routes, Route } from 'react-router-dom'
import FestivalListPage from './pages/FestivalListPage'
import FestivalProgramPage from './pages/FestivalProgramPage'

// navigazione client-side con React Router (lez. 12-3 slide 13-14)
function App() {
  return (
    <BrowserRouter>
      <header>
        <a href="http://localhost:8080/">SiwFestival</a>
        <span> - Programma dei festival (React)</span>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<FestivalListPage />} />
          <Route path="/festivals/:id" element={<FestivalProgramPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App

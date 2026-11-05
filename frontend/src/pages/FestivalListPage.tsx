import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { Festival } from '../types'
import { getFestivals } from '../services/festivalService'

function FestivalListPage() {
  const [festivals, setFestivals] = useState<Festival[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // al primo render (array di dipendenze vuoto) chiama GET /api/festivals
  useEffect(() => {
    getFestivals()
      .then(data => setFestivals(data))
      .catch(() => setError('Impossibile caricare i festival'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <p>Caricamento...</p>
  if (error) return <p className="error">{error}</p>

  return (
    <div>
      <h1>Festival</h1>
      <ul className="cards">
        {festivals.map(festival => (
          <li key={festival.id}>
            <Link to={`/festivals/${festival.id}`}>{festival.name} {festival.year}</Link>
            <div>{festival.city}, {festival.startDate} - {festival.endDate}</div>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default FestivalListPage

import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import type { Festival, Movie, Screening } from '../types'
import { getFestival, getFestivalMovies, getFestivalScreenings } from '../services/festivalService'

function FestivalProgramPage() {
  const { id } = useParams()   // l'id preso dall'URL /festivals/:id
  const [festival, setFestival] = useState<Festival | null>(null)
  const [movies, setMovies] = useState<Movie[]>([])
  const [screenings, setScreenings] = useState<Screening[]>([])
  const [selectedDate, setSelectedDate] = useState('')   // filtro per giorno ('' = tutti i giorni)
  const [error, setError] = useState<string | null>(null)

  // si esegue al primo render e ogni volta che cambia l'id nell'URL
  useEffect(() => {
    const festivalId = Number(id)
    Promise.all([getFestival(festivalId), getFestivalMovies(festivalId), getFestivalScreenings(festivalId)])
      .then(([f, m, s]) => {
        setFestival(f)
        setMovies(m)
        setScreenings(s)
      })
      .catch(() => setError('Festival non trovato'))
  }, [id])

  if (error) return <p className="error">{error}</p>
  if (!festival) return <p>Caricamento...</p>

  // i giorni che hanno almeno una proiezione, senza ripetizioni, per la tendina del filtro
  const dates = [...new Set(screenings.map(s => s.date))]
  // filtro lato client: i dati sono già in memoria, nessuna nuova chiamata al server
  const filtered = selectedDate === '' ? screenings : screenings.filter(s => s.date === selectedDate)

  return (
    <div>
      <Link to="/">&larr; Tutti i festival</Link>
      <h1>{festival.name} ({festival.year})</h1>
      <p>{festival.city}, dal {festival.startDate} al {festival.endDate}</p>
      <p>{festival.description}</p>

      <h2>Film in concorso</h2>
      <ul className="cards">
        {movies.map(movie => (
          <li key={movie.id}>
            <strong>{movie.title}</strong> ({movie.year})
            {movie.director && <span> di {movie.director.name} {movie.director.surname}</span>}
          </li>
        ))}
      </ul>

      <h2>Programma delle proiezioni</h2>
      <label>
        Giorno:{' '}
        <select value={selectedDate} onChange={e => setSelectedDate(e.target.value)}>
          <option value="">Tutti i giorni</option>
          {dates.map(date => <option key={date} value={date}>{date}</option>)}
        </select>
      </label>
      {filtered.length === 0 && <p>Nessuna proiezione</p>}
      <ul className="cards">
        {filtered.map(screening => (
          <li key={screening.id} className={screening.status === 'CANCELLED' ? 'cancelled' : ''}>
            <strong>{screening.date} ore {screening.time.substring(0, 5)}</strong>
            {' - '}{screening.movie.title} ({screening.movie.duration} min)
            {' - '}{screening.room.name}
            {' - '}{screening.status}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default FestivalProgramPage

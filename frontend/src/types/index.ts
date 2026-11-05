// Le interfacce replicano i campi del JSON restituito dalle API Spring Boot (lez. 12-2 slide 19)
export interface Festival {
  id: number
  name: string
  year: number
  city: string
  startDate: string   // "YYYY-MM-DD"
  endDate: string
  description: string
}

export interface Artist {
  id: number
  name: string
  surname: string
  dateOfBirth: string
  nationality: string
}

export interface Movie {
  id: number
  title: string
  year: number
  duration: number
  genre: string
  productionCountry: string
  director?: Artist   // il regista può mancare
}

export interface Room {
  id: number
  name: string
  address: string
  capacity: number
}

export interface Screening {
  id: number
  date: string        // "YYYY-MM-DD"
  time: string        // "HH:mm:ss"
  status: 'SCHEDULED' | 'COMPLETED' | 'CANCELLED'
  movie: Movie
  room: Room
}

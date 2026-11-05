import api from './api'
import type { Festival, Movie, Screening } from '../types'

// i componenti chiamano queste funzioni, non api.get(...) direttamente (lez. 12-2 slide 10 e 19)

export async function getFestivals(): Promise<Festival[]> {
  const { data } = await api.get<Festival[]>('/festivals')
  return data
}

export async function getFestival(id: number): Promise<Festival> {
  const { data } = await api.get<Festival>(`/festivals/${id}`)
  return data
}

export async function getFestivalMovies(id: number): Promise<Movie[]> {
  const { data } = await api.get<Movie[]>(`/festivals/${id}/movies`)
  return data
}

export async function getFestivalScreenings(id: number): Promise<Screening[]> {
  const { data } = await api.get<Screening[]>(`/festivals/${id}/screenings`)
  return data
}

import axios from 'axios'

// istanza Axios creata una volta sola: la baseURL si scrive in un posto solo (lez. 12-2 slide 19)
const api = axios.create({ baseURL: 'http://localhost:8080/api' })

export default api

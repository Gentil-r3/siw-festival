# SiwFestival – Gentile 607045

Progetto del corso **Sistemi Informativi su Web** (Roma Tre, prof. Merialdo), sessione novembre 2026.
Sistema per la gestione di **festival cinematografici**: festival, film, artisti (registi e attori con il personaggio), sale, programma delle proiezioni e recensioni degli utenti registrati.

## Tecnologie
- Java 21, Spring Boot 4.0 (Web MVC, Data JPA, Validation, Security), Thymeleaf
- PostgreSQL, Hibernateee
- React + TypeScript (Vite), Axios, React Router

## Avvio
1. **Database:** PostgreSQL con un database `siwfestival` (utente `postgres` / password `postgres`).
   Sul mio PC PostgreSQL è sulla porta **5433**: se il vostro è sulla 5432, cambiate la riga
   `spring.datasource.url` in `src/main/resources/application.properties`.
2. **Backend:** `./mvnw spring-boot:run` (oppure Run di `FestivalApplication`) → http://localhost:8080
   Con `ddl-auto=create` le tabelle vengono ricreate a ogni avvio e riempite da `import.sql`.
3. **Frontend React:** `cd frontend`, `npm install`, `npm run dev` → http://localhost:5173
   (raggiungibile anche dal link "Programma dei festival (React)" nella home).

### Utenti di prova
| Username | Password | Ruolo |
|---|---|---|
| andrea | andrea | ADMIN |
| mario | mario | utente registrato |
| anna | anna | utente registrato |

## Modello dei dati
Entità: `Festival`, `Movie`, `Artist`, `Interpretation`, `Room`, `Screening`, `Review`, `User`, `Credentials`.
- `Festival` ↔ `Movie`: **@ManyToMany** (tabella `festival_movie`, lato proprietario `Festival.movies`)
- `Movie` → `Artist` (regista): **@ManyToOne** `director`; inverso `Artist.directedMovies`
- Attori con personaggio: entità intermedia `Interpretation` (`characterName`) con due @ManyToOne verso `Movie` e `Artist`
- `Screening`: @ManyToOne verso `Festival`, `Movie`, `Room`; stato come enum `ScreeningStatus` (`SCHEDULED`, `COMPLETED`, `CANCELLED`) salvato come stringa
- `Review`: @ManyToOne verso `Movie` e `User`; vincolo `UNIQUE(movie_id, user_id)` (al massimo una recensione per utente per film)
- `Credentials` (username, password BCrypt, ruolo) @OneToOne `User`

## Casi d'uso
- **Pubblici:** elenco festival, dettaglio festival con film partecipanti e programma delle proiezioni, elenco e dettaglio film (regista, attori, recensioni)
- **Utenti registrati:** inserimento di una recensione (una per film), modifica e cancellazione **solo delle proprie** recensioni
- **Amministratore:** inserimento, modifica e cancellazione film; programmazione di una proiezione (controllo che il film partecipi al festival, che la data sia nel periodo del festival e che non ci siano sovrapposizioni nella stessa sala)

## Scelte progettuali
### Transazioni (§7)
- Metodi di sola lettura dei service: `@Transactional(readOnly = true)`, isolamento di default di PostgreSQL (READ COMMITTED), sufficiente per letture non usate per decisioni.
- Salvataggio/cancellazione film e recensioni: `@Transactional` con READ COMMITTED (nessuna decisione critica basata su letture ripetute; l'unicità della recensione è garantita anche dal vincolo UNIQUE nel DB).
- **Programmazione di una proiezione** (`ScreeningService.schedule`): operazione multi-entità atomica in una sola transazione con **`Isolation.SERIALIZABLE`**, perché legge le proiezioni della sala per decidere se può scrivere: con READ COMMITTED due amministratori potrebbero vedere la sala libera contemporaneamente e salvare due proiezioni sovrapposte (inserimento fantasma). In caso di violazione di una regola viene lanciata `ScreeningNotAllowedException` → rollback.

### Strategie di fetch e problema N+1 (§8.1)
- `Movie.director` è `@ManyToOne(fetch = LAZY)`: l'elenco `/movies` non mostra il regista, con il default EAGER faceva 1 + N query (una per regista), con LAZY una sola.
- Dove il regista serve per molti film (film di un festival, pagina e API) si usa `LEFT JOIN FETCH` (`MovieRepository.findByFestivalWithDirector`); per il dettaglio del film `@EntityGraph(attributePaths = "director")` (`findWithDirectorById`).
- Programma del festival: una query con `JOIN FETCH` su film, regista e sala, ordinata per data e ora (`ScreeningRepository.findByFestivalWithMovieAndRoom`).
- Collezioni (`@OneToMany`, `@ManyToMany`): LAZY (default).
- Cascade: `ALL + orphanRemoval` solo dove c'è composizione (`Movie.interpretations`, `Movie.reviews`); `REMOVE` su `Movie.screenings`; nessun cascade sulla @ManyToMany (cancellando un film si rimuovono solo le righe di `festival_movie`, dal lato proprietario).

### Script di confronto (§8.2)
`FestivalApplication` implementa `CommandLineRunner`: **all'avvio** stampa su console, per i film della Mostra di Venezia con i loro registi, tre strategie (LAZY, JOIN FETCH, EntityGraph) con film caricati, oggetti caricati, numero di query SQL (statistiche di Hibernate, `generate_statistics=true`) e tempo (`StopWatch`). Risultato: LAZY 4 query (1+N), JOIN FETCH 1, EntityGraph 1.

## API REST (§10)
| Metodo | URL | Descrizione |
|---|---|---|
| GET | `/api/festivals` | elenco festival |
| GET | `/api/festivals/{id}` | un festival (404 se non esiste) |
| GET | `/api/festivals/{id}/movies` | film del festival con il regista |
| GET | `/api/festivals/{id}/screenings` | programma delle proiezioni |
| GET | `/api/movies/{id}` | un film con il regista (404 se non esiste) |
| GET | `/api/movies/{id}/reviews` | recensioni del film con l'autore |

Le collezioni e i riferimenti all'indietro sono esclusi dal JSON con `@JsonIgnore` (evita i cicli); CORS abilitato per `http://localhost:5173` su `/api/**`.

## Frontend React (§9)
Cartella `frontend`: **programma dei festival**. Elenco dei festival e, per ogni festival, film in concorso e programma delle proiezioni con filtro per giorno (lato client). I dati arrivano dalle API REST con Axios.

## Sicurezza
Spring Security con login da form, password cifrate con BCrypt, ruoli `DEFAULT` e `ADMIN`; `/admin/**` solo per gli amministratori; le recensioni richiedono il login e il service verifica che solo l'autore possa modificarle o cancellarle (altrimenti 403). Protezione CSRF attiva su tutte le form.

## Malfunzionamenti noti / limiti
- Registrazione con username o email già esistenti: errore 500 (manca il controllo nel service).
- Il controllo delle sovrapposizioni considera solo le proiezioni dello stesso giorno: una proiezione del giorno prima che finisce dopo mezzanotte non viene considerata.
- Gli errori 404 delle API REST restituiscono la pagina HTML di errore (il codice di stato è corretto, il corpo non è JSON).
- Non sono implementate le pagine di creazione/modifica di festival e sale, l'associazione film–festival da interfaccia e la modifica/cancellazione delle proiezioni (i dati sono in `import.sql`).
- I pulsanti Modifica/Cancella film sono visibili anche ai non amministratori (che ricevono 403).

package it.uniroma3.siw.festival.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.exception.DuplicateMovieException;
import it.uniroma3.siw.festival.exception.MovieNotFoundException;
import it.uniroma3.siw.festival.model.Festival;
import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    //Costruttore: riceve il repository da Spring e lo conserva nel campo. 
    // final vuol dire che dopo non può più cambiare.

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true) // solo lettura: non può creare conflitti (lez. 07 slide 56)
    public List<Movie> findAll() { //tutti i film (lo metto per /movies)
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Movie findById(Long id) {
        // film + regista in una query (EntityGraph): il dettaglio mostra sempre il regista
        // se il film non esiste è un caso d'uso: lancio l'eccezione, la gestisce il GlobalExceptionHandler (404)
        Optional<Movie> optionalMovie = movieRepository.findWithDirectorById(id);
        if (optionalMovie.isPresent()) {
            return optionalMovie.get();
        } else {
            throw new MovieNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    public List<Movie> findByFestival(Long festivalId) {
        // film del festival con i registi in una sola query (JOIN FETCH, passo 20)
        return movieRepository.findByFestivalWithDirector(festivalId);
    }

    @Transactional
    public Movie save(Movie movie) throws DuplicateMovieException {
        boolean duplicate = movie.getId() == null  //controllo se film è nuovo o da modificare 
            ? movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())  //NUOVO
            : movieRepository.existsByTitleAndYearAndIdNot(movie.getTitle(), movie.getYear(), movie.getId()); // SE È UNA MODIFICA
        if (duplicate) {
            throw new DuplicateMovieException(movie.getTitle(), movie.getYear());
            //LANCIO ECCEZIONE E IL FILM NON VIENE SALVATO 
        }
        if (movie.getId() != null) {
            // modifica: la form non invia le collezioni; se restassero null, il save (merge) le svuoterebbe
            // e orphanRemoval cancellerebbe attori e recensioni. Tengo quelle del film già salvato.
            Movie existing = findById(movie.getId());
            movie.setFestivals(existing.getFestivals());
            movie.setInterpretations(existing.getInterpretations());
            movie.setScreenings(existing.getScreenings());
            movie.setReviews(existing.getReviews());
        }
        return movieRepository.save(movie);
    }

    @Transactional //viene messo nei metodi che scrivono cioè save e deledetedById
    public void deleteById(Long id) {
        Movie movie = findById(id);
        // Festival è il lato proprietario della @ManyToMany: tolgo il film da ogni festival,
        // così Hibernate cancella le righe di festival_movie
        for (Festival festival : movie.getFestivals()) {
            festival.getMovies().remove(movie);
        }
        // proiezioni (cascade REMOVE), personaggi e recensioni (cascade ALL) vengono cancellati con il film
        movieRepository.deleteById(id);
    }
}

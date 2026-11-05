package it.uniroma3.siw.festival.exception;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long id) {
        super("Il film con ID " + id + " non esiste.");
    }
}

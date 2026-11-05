package it.uniroma3.siw.festival.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(Long id) {
        super("La recensione con ID " + id + " non esiste.");
    }
}

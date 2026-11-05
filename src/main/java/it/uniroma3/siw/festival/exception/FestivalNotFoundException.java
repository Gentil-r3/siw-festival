package it.uniroma3.siw.festival.exception;

public class FestivalNotFoundException extends RuntimeException {

    public FestivalNotFoundException(Long id) {
        super("Il festival con ID " + id + " non esiste.");
    }
}

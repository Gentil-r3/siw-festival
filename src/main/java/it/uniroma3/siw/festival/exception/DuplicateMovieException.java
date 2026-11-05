package it.uniroma3.siw.festival.exception;

public class DuplicateMovieException extends RuntimeException {

    public DuplicateMovieException(String title, Integer year) {
        super("Il film '" + title + "' (" + year + ") è già presente nel sistema");
        // super(...): passa il messaggio di errore alla classe padre. 
        // Più avanti lo leggeremo con e.getMessage()
    }
}

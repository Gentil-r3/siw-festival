package it.uniroma3.siw.festival.exception;

public class DuplicateReviewException extends RuntimeException {

    public DuplicateReviewException() {
        super("Hai già scritto una recensione per questo film");
    }
}

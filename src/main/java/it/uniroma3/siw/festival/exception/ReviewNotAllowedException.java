package it.uniroma3.siw.festival.exception;

public class ReviewNotAllowedException extends RuntimeException {

    public ReviewNotAllowedException() {
        super("Puoi modificare o cancellare solo le tue recensioni.");
    }
}

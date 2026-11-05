package it.uniroma3.siw.festival.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // la risorsa applicativa non esiste (es. /movies/999): 404 con il messaggio dell'eccezione
    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMovieNotFound(MovieNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(FestivalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFestivalNotFound(FestivalNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleReviewNotFound(ReviewNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    // un utente prova a modificare/cancellare la recensione di un altro: 403
    @ExceptionHandler(ReviewNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleReviewNotAllowed(ReviewNotAllowedException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/403";
    }

    // l'URL non esiste proprio (es. /moviess): 404 di routing
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException e, Model model) {
        model.addAttribute("errorMessage", "Pagina non trovata");
        return "error/404";
    }

    // qualsiasi altro errore inatteso: 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("errorMessage", "Si è verificato un errore interno.");
        return "error/500";
    }
}

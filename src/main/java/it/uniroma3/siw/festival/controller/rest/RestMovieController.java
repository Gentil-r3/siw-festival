package it.uniroma3.siw.festival.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.model.Review;
import it.uniroma3.siw.festival.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class RestMovieController {
    private final MovieService movieService;

    public RestMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET /api/movies/{id}: un film con il regista (404 se non esiste)
    @GetMapping("/{id}")
    public Movie show(@PathVariable Long id) {
        return movieService.findById(id);
    }

    // GET /api/movies/{id}/reviews: le recensioni del film, con l'autore (nome e cognome)
    @GetMapping("/{id}/reviews")
    public List<Review> reviews(@PathVariable Long id) {
        return movieService.findById(id).getReviews();
    }
}

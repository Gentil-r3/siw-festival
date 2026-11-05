package it.uniroma3.siw.festival.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.festival.model.Festival;
import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.model.Screening;
import it.uniroma3.siw.festival.service.FestivalService;
import it.uniroma3.siw.festival.service.MovieService;
import it.uniroma3.siw.festival.service.ScreeningService;

@RestController // restituisce oggetti, che Spring converte in JSON (lez. 11 slide 21)
@RequestMapping("/api/festivals")
public class RestFestivalController {
    private final FestivalService festivalService;
    private final MovieService movieService;
    private final ScreeningService screeningService;

    public RestFestivalController(FestivalService festivalService, MovieService movieService,
            ScreeningService screeningService) {
        this.festivalService = festivalService;
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    // GET /api/festivals: tutti i festival
    @GetMapping
    public List<Festival> list() {
        return festivalService.findAll();
    }

    // GET /api/festivals/{id}: un festival (404 se non esiste)
    @GetMapping("/{id}")
    public Festival show(@PathVariable Long id) {
        return festivalService.findById(id);
    }

    // GET /api/festivals/{id}/movies: i film del festival con il regista
    @GetMapping("/{id}/movies")
    public List<Movie> movies(@PathVariable Long id) {
        festivalService.findById(id); // se il festival non esiste: 404
        return movieService.findByFestival(id);
    }

    // GET /api/festivals/{id}/screenings: il programma delle proiezioni
    @GetMapping("/{id}/screenings")
    public List<Screening> screenings(@PathVariable Long id) {
        festivalService.findById(id); // se il festival non esiste: 404
        return screeningService.findByFestival(id);
    }
}

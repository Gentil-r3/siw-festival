package it.uniroma3.siw.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.festival.exception.DuplicateMovieException;
import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.service.ArtistService;
import it.uniroma3.siw.festival.service.MovieService;
import jakarta.validation.Valid;

//il costruttore riceve movieservice e artistservice da spring 
@Controller
public class MovieController {
    private final MovieService movieService;
    private final ArtistService artistService;

    public MovieController(MovieService movieService, ArtistService artistService) {
        this.movieService = movieService;
        this.artistService = artistService;
    }

    @GetMapping("/movies")
    public String list(Model model) {   //risponde a GET/movies , mette nel modello 
        model.addAttribute("movies", movieService.findAll());//la lista di tutti i 
        return "movies/list";//i film col nome movies  
    }

    @GetMapping("/admin/movies/new")
    public String createForm(Model model) {  // mette nel model un Movie vuoto , che la form
                                            //userà come th:object e mostra la form  
        model.addAttribute("movie", new Movie());
        model.addAttribute("artists", artistService.findAll()); //la lista degli artisti per la tendina del regista
        return "admin/movies/form";
    }

    @PostMapping("/admin/movies")
    public String save(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
        // @Valid: Spring controlla i vincoli dell'entità; gli errori finiscono in bindingResult
        // (BindingResult va subito dopo l'oggetto validato, prima di Model)
        if (bindingResult.hasErrors()) {
            model.addAttribute("artists", artistService.findAll());
            return "admin/movies/form";
        }
        try {
            movieService.save(movie);
            return "redirect:/movies";
        } catch (DuplicateMovieException e) {
            // regola di business violata nel service: errore globale con il codice di messages.properties
            bindingResult.reject("movie.duplicate");
            model.addAttribute("artists", artistService.findAll());
            return "admin/movies/form";
        }
    }

    @GetMapping("/admin/movies/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) { // se non esiste: 404 dal GlobalExceptionHandler
        model.addAttribute("movie", movieService.findById(id));
        model.addAttribute("artists", artistService.findAll());
        return "admin/movies/form";
    }

    @PostMapping("/admin/movies/{id}/delete")
    public String delete(@PathVariable Long id) {// cancella film con quell`id e redirect alla lista 
        movieService.deleteById(id);
        return "redirect:/movies";
    }

    @GetMapping("/movies/{id}")//risponde a GET/movies/{id}: mette il film nel model e usa movies/show.html
    public String show(@PathVariable Long id, Model model) { // se non esiste: 404 dal GlobalExceptionHandler
        model.addAttribute("movie", movieService.findById(id));
        return "movies/show";
    }
}

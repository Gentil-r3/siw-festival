package it.uniroma3.siw.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.festival.service.FestivalService;
import it.uniroma3.siw.festival.service.MovieService;

@Controller
public class FestivalController {
    private final FestivalService festivalService;
    private final MovieService movieService;

    public FestivalController(FestivalService festivalService, MovieService movieService) {
        this.festivalService = festivalService;
        this.movieService = movieService;
    }

    @GetMapping("/festivals")
    public String list(Model model) { // elenco di tutti i festival
        model.addAttribute("festivals", festivalService.findAll());
        return "festivals/list";
    }

    @GetMapping("/festivals/{id}")
    public String show(@PathVariable Long id, Model model) { // dettaglio: dati, film con regista, programma
        model.addAttribute("festival", festivalService.findById(id)); // se non esiste: 404
        model.addAttribute("movies", movieService.findByFestival(id)); // film + registi con JOIN FETCH
        return "festivals/show";
    }
}

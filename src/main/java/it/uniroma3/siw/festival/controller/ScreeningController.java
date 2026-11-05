package it.uniroma3.siw.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.festival.exception.ScreeningNotAllowedException;
import it.uniroma3.siw.festival.model.Screening;
import it.uniroma3.siw.festival.service.FestivalService;
import it.uniroma3.siw.festival.service.MovieService;
import it.uniroma3.siw.festival.service.RoomService;
import it.uniroma3.siw.festival.service.ScreeningService;

@Controller
public class ScreeningController {
    private final ScreeningService screeningService;
    private final FestivalService festivalService;
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningController(ScreeningService screeningService, FestivalService festivalService,
            MovieService movieService, RoomService roomService) {
        this.screeningService = screeningService;
        this.festivalService = festivalService;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @GetMapping("/admin/festivals/{festivalId}/screenings/new")
    public String createForm(@PathVariable Long festivalId, Model model) {
        model.addAttribute("festival", festivalService.findById(festivalId)); // se non esiste: 404
        model.addAttribute("screening", new Screening());
        model.addAttribute("movies", movieService.findByFestival(festivalId)); // solo i film del festival
        model.addAttribute("rooms", roomService.findAll());
        return "admin/screenings/form";
    }

    @PostMapping("/admin/festivals/{festivalId}/screenings")
    public String save(@PathVariable Long festivalId, @ModelAttribute("screening") Screening screening,
            BindingResult bindingResult, Model model) {
        try {
            screeningService.schedule(festivalId, screening);
            return "redirect:/festivals/" + festivalId;
        } catch (ScreeningNotAllowedException e) {
            // come movie02 con DuplicateMovieException: errore globale e di nuovo la form
            bindingResult.reject("screening.notAllowed", e.getMessage());
            model.addAttribute("festival", festivalService.findById(festivalId));
            model.addAttribute("movies", movieService.findByFestival(festivalId));
            model.addAttribute("rooms", roomService.findAll());
            return "admin/screenings/form";
        }
    }
}

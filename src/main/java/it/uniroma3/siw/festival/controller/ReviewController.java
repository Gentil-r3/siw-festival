package it.uniroma3.siw.festival.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.festival.exception.DuplicateReviewException;
import it.uniroma3.siw.festival.model.Review;
import it.uniroma3.siw.festival.service.MovieService;
import it.uniroma3.siw.festival.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;

    public ReviewController(ReviewService reviewService, MovieService movieService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    // username dell'utente loggato (lez. 10 slide 20): gli URL /user/** richiedono il login
    private String currentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping("/user/movies/{movieId}/reviews/new")
    public String createForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movie", movieService.findById(movieId));
        model.addAttribute("review", new Review());
        return "reviews/form";
    }

    @PostMapping("/user/movies/{movieId}/reviews")
    public String create(@PathVariable Long movieId, @Valid @ModelAttribute("review") Review review,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movieService.findById(movieId));
            return "reviews/form";
        }
        try {
            reviewService.create(movieId, review, currentUsername());
            return "redirect:/movies/" + movieId;
        } catch (DuplicateReviewException e) {
            bindingResult.reject("review.duplicate");
            model.addAttribute("movie", movieService.findById(movieId));
            return "reviews/form";
        }
    }

    @GetMapping("/user/reviews/{reviewId}/edit")
    public String editForm(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.findForEdit(reviewId, currentUsername()); // se non è l'autore: 403
        model.addAttribute("review", review);
        model.addAttribute("movie", review.getMovie());
        return "reviews/form";
    }

    @PostMapping("/user/reviews/{reviewId}")
    public String update(@PathVariable Long reviewId, @Valid @ModelAttribute("review") Review review,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", reviewService.findById(reviewId).getMovie());
            return "reviews/form";
        }
        Review updated = reviewService.update(reviewId, review, currentUsername());
        return "redirect:/movies/" + updated.getMovie().getId();
    }

    @PostMapping("/user/reviews/{reviewId}/delete")
    public String delete(@PathVariable Long reviewId) {
        Long movieId = reviewService.delete(reviewId, currentUsername());
        return "redirect:/movies/" + movieId;
    }
}

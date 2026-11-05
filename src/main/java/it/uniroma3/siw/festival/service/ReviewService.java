package it.uniroma3.siw.festival.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.exception.DuplicateReviewException;
import it.uniroma3.siw.festival.exception.MovieNotFoundException;
import it.uniroma3.siw.festival.exception.ReviewNotAllowedException;
import it.uniroma3.siw.festival.exception.ReviewNotFoundException;
import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.model.Review;
import it.uniroma3.siw.festival.model.User;
import it.uniroma3.siw.festival.repository.CredentialsRepository;
import it.uniroma3.siw.festival.repository.MovieRepository;
import it.uniroma3.siw.festival.repository.ReviewRepository;

@Service
public class ReviewService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

	private final ReviewRepository reviewRepository;
	private final MovieRepository movieRepository;
	private final CredentialsRepository credentialsRepository;

	public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository,
			CredentialsRepository credentialsRepository) {
		this.reviewRepository = reviewRepository;
		this.movieRepository = movieRepository;
		this.credentialsRepository = credentialsRepository;
	}

	@Transactional(readOnly = true)
	public Review findById(Long id) {
		Optional<Review> optionalReview = reviewRepository.findById(id);
		if (optionalReview.isPresent()) {
			return optionalReview.get();
		} else {
			throw new ReviewNotFoundException(id);
		}
	}

	// recensione da modificare: solo se l'utente loggato ne è l'autore
	@Transactional(readOnly = true)
	public Review findForEdit(Long reviewId, String username) {
		Review review = findById(reviewId);
		checkAuthor(review, username);
		return review;
	}

	@Transactional
	public Review create(Long movieId, Review review, String username) {
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));
		User user = getUser(username);
		// regola di business: al massimo una recensione per utente per film
		if (reviewRepository.existsByMovieIdAndUserId(movieId, user.getId())) {
			throw new DuplicateReviewException();
		}
		review.setMovie(movie);
		review.setUser(user);
		review.setDate(LocalDate.now());
		logger.info("Nuova recensione: film={}, utente={}, voto={}", movie.getTitle(), username, review.getRating());
		return reviewRepository.save(review);
	}

	@Transactional
	public Review update(Long reviewId, Review data, String username) {
		Review review = findById(reviewId);
		checkAuthor(review, username);
		review.setText(data.getText());
		review.setRating(data.getRating());
		review.setDate(LocalDate.now());
		return reviewRepository.save(review);
	}

	// restituisce l'id del film, per tornare alla sua pagina
	@Transactional
	public Long delete(Long reviewId, String username) {
		Review review = findById(reviewId);
		checkAuthor(review, username);
		Long movieId = review.getMovie().getId();
		reviewRepository.delete(review);
		logger.info("Recensione {} cancellata da {}", reviewId, username);
		return movieId;
	}

	// l'utente collegato alle credenziali con questo username
	private User getUser(String username) {
		return credentialsRepository.findByUsername(username).get().getUser();
	}

	// solo l'autore può modificare o cancellare la recensione
	private void checkAuthor(Review review, String username) {
		if (!review.getUser().equals(getUser(username))) {
			logger.warn("L'utente {} ha provato a modificare la recensione {} di un altro", username, review.getId());
			throw new ReviewNotAllowedException();
		}
	}
}

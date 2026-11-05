package it.uniroma3.siw.festival.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.festival.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	// esiste già una recensione di questo utente per questo film?
	boolean existsByMovieIdAndUserId(Long movieId, Long userId);

}

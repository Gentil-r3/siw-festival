package it.uniroma3.siw.festival.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.festival.model.Festival;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

	Optional<Festival> findByName(String name);

}

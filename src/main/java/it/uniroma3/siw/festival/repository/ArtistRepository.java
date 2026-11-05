package it.uniroma3.siw.festival.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.festival.model.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

	List<Artist> findByNameContainingIgnoreCase(String name); 
    //cerca gli artisti il cui nome contiene il testo senza distringuere maiuscole e minuscole
	boolean existsByNameAndDateOfBirth(String name, LocalDate dateOfBirth);
    //controllo dei duplicati per gli artisti.
}

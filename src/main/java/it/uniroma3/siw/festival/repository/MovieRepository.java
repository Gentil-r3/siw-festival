package it.uniroma3.siw.festival.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.festival.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	List<Movie> findByTitleContainingIgnoreCase(String title);
	boolean existsByTitleAndYear(String title, Integer year);
	boolean existsByTitleAndYearAndIdNot(String title, Integer year, Long id);

	// i film di un festival (query costruita dal nome del metodo)
	List<Movie> findByFestivalsId(Long festivalId);

	// i film di un festival con il regista caricato nella stessa query (JOIN FETCH)
	@Query("SELECT m FROM Movie m JOIN m.festivals f LEFT JOIN FETCH m.director WHERE f.id = :festivalId") 
	// JOIN m.festivals f significa che dal film passa ai suoi festival e chiama f ognuno di loro
	// WHERE f.id= :festivalId significa che prende solo i film che hanno un festival con quell'id
	// LEFT JOIN FETCH m.director carica anche il regista .
	List<Movie> findByFestivalWithDirector(@Param("festivalId") Long festivalId);

	// i film di un festival con il regista caricato nella stessa query (EntityGraph)
	@EntityGraph(attributePaths = "director")
	List<Movie> findWithDirectorByFestivalsId(Long festivalId);

	// un film con il suo regista nella stessa query: il dettaglio (pagina e API) mostra sempre il regista
	@EntityGraph(attributePaths = "director")
	Optional<Movie> findWithDirectorById(Long id);
	
}

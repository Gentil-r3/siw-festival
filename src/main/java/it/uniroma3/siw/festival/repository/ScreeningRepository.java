package it.uniroma3.siw.festival.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.festival.model.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

	// proiezioni della stessa sala nello stesso giorno (per controllare le sovrapposizioni)
	List<Screening> findByRoomIdAndDate(Long roomId, LocalDate date);

	// programma di un festival: film, regista e sala nella stessa query, in ordine di data e ora
	@Query("SELECT s FROM Screening s JOIN FETCH s.movie m LEFT JOIN FETCH m.director JOIN FETCH s.room "
			+ "WHERE s.festival.id = :festivalId ORDER BY s.date, s.time")
	List<Screening> findByFestivalWithMovieAndRoom(@Param("festivalId") Long festivalId);

}
//nuova query per il programma di un festival, con i film ordinati per titolo e
//  regista, e le sale ordinate per nome , in una sola query 
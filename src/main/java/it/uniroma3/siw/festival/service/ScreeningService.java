package it.uniroma3.siw.festival.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.exception.FestivalNotFoundException;
import it.uniroma3.siw.festival.exception.ScreeningNotAllowedException;
import it.uniroma3.siw.festival.model.Festival;
import it.uniroma3.siw.festival.model.Screening;
import it.uniroma3.siw.festival.model.ScreeningStatus;
import it.uniroma3.siw.festival.repository.FestivalRepository;
import it.uniroma3.siw.festival.repository.ScreeningRepository;

@Service
public class ScreeningService {

	private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);

	private final ScreeningRepository screeningRepository;
	private final FestivalRepository festivalRepository;

	public ScreeningService(ScreeningRepository screeningRepository, FestivalRepository festivalRepository) {
		this.screeningRepository = screeningRepository;
		this.festivalRepository = festivalRepository;
	}

	// programma del festival, con film, regista e sala in una sola query
	@Transactional(readOnly = true)
	public List<Screening> findByFestival(Long festivalId) {
		return screeningRepository.findByFestivalWithMovieAndRoom(festivalId);
	}

	// legge i dati (proiezioni della sala) per decidere se può scrivere: SERIALIZABLE (lez. 07 slide 49)
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Screening schedule(Long festivalId, Screening screening) {
		logger.info("Programmazione proiezione richiesta: festival id={}", festivalId);
		if (screening.getDate() == null || screening.getTime() == null
				|| screening.getMovie() == null || screening.getRoom() == null) {
			throw new ScreeningNotAllowedException("Compila tutti i campi");
		}
		Festival festival = festivalRepository.findById(festivalId)
				.orElseThrow(() -> new FestivalNotFoundException(festivalId));

		// 1. il film deve partecipare al festival
		if (!festival.getMovies().contains(screening.getMovie())) {
			throw new ScreeningNotAllowedException("Il film non partecipa a questo festival");
		}

		// 2. la data deve cadere nel periodo del festival
		if (screening.getDate().isBefore(festival.getStartDate()) || screening.getDate().isAfter(festival.getEndDate())) {
			throw new ScreeningNotAllowedException("La data è fuori dal periodo del festival");
		}

		// 3. nessuna sovrapposizione nella stessa sala, lo stesso giorno
		// data + ora (LocalDateTime): così una proiezione che finisce dopo mezzanotte finisce il giorno dopo
		LocalDateTime start = LocalDateTime.of(screening.getDate(), screening.getTime());
		LocalDateTime end = start.plusMinutes(screening.getMovie().getDuration());
		for (Screening other : screeningRepository.findByRoomIdAndDate(screening.getRoom().getId(), screening.getDate())) {
			if (other.getStatus() != ScreeningStatus.CANCELLED) {
				LocalDateTime otherStart = LocalDateTime.of(other.getDate(), other.getTime());
				LocalDateTime otherEnd = otherStart.plusMinutes(other.getMovie().getDuration());
				if (start.isBefore(otherEnd) && otherStart.isBefore(end)) {
					logger.warn("Sala {} occupata il {} alle {}", screening.getRoom().getName(), screening.getDate(), other.getTime());
					throw new ScreeningNotAllowedException("La sala " + screening.getRoom().getName()
							+ " è già occupata da " + other.getMovie().getTitle() + " alle " + other.getTime());
				}
			}
		}

		// 4. tutto ok: salvo la nuova proiezione
		screening.setFestival(festival);
		screening.setStatus(ScreeningStatus.SCHEDULED);
		Screening saved = screeningRepository.save(screening);
		logger.info("Proiezione programmata: id={}, film={}, sala={}, {} {}", saved.getId(),
				screening.getMovie().getTitle(), screening.getRoom().getName(), screening.getDate(), screening.getTime());
		return saved;
	}
}

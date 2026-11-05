package it.uniroma3.siw.festival.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.model.Artist;
import it.uniroma3.siw.festival.repository.ArtistRepository;

@Service
public class ArtistService {

	private final ArtistRepository artistRepository;

	public ArtistService(ArtistRepository artistRepository) {
		this.artistRepository = artistRepository;
	}

	@Transactional(readOnly = true)
	public List<Artist> findAll() {
		return artistRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Artist> findById(Long id) {
		return artistRepository.findById(id);
	}
}

package it.uniroma3.siw.festival.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.exception.FestivalNotFoundException;
import it.uniroma3.siw.festival.model.Festival;
import it.uniroma3.siw.festival.repository.FestivalRepository;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @Transactional(readOnly = true)
    public List<Festival> findAll() { // tutti i festival (per /festivals)
        return festivalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Festival findById(Long id) { // un festival per id; se non esiste 404
        Optional<Festival> optionalFestival = festivalRepository.findById(id);
        if (optionalFestival.isPresent()) {
            return optionalFestival.get();
        } else {
            throw new FestivalNotFoundException(id);
        }
    }
}

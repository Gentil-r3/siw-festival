package it.uniroma3.siw.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.model.Room;
import it.uniroma3.siw.festival.repository.RoomRepository;

@Service
public class RoomService {

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Transactional(readOnly = true)
	public List<Room> findAll() {
		return roomRepository.findAll();
	}
}

package it.uniroma3.siw.festival.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.festival.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}

package it.uniroma3.siw.festival.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.festival.model.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

	Optional<Credentials> findByUsername(String username); // lez. 10 slide 12

}

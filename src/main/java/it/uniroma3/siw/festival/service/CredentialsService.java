package it.uniroma3.siw.festival.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.festival.model.Credentials;
import it.uniroma3.siw.festival.repository.CredentialsRepository;

@Service
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;

    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE); // chi si registra è sempre utente normale, mai admin
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword())); // salvo la password cifrata
        return this.credentialsRepository.save(credentials); // cascade: salva anche lo User
    }
}

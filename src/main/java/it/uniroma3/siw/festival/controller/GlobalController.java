package it.uniroma3.siw.festival.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.festival.model.Credentials;
import it.uniroma3.siw.festival.model.User;
import it.uniroma3.siw.festival.service.CredentialsService;

@ControllerAdvice
public class GlobalController {

    private final CredentialsService credentialsService;

    public GlobalController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    // mette in ogni modello l'attributo "userDetails": l'utente loggato, oppure null
    @ModelAttribute("userDetails")
    public UserDetails getUser() {
        UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) authentication.getPrincipal();
        }
        return user;
    }

    // mette in ogni modello "currentUser": lo User loggato (per riconoscere le sue recensioni), oppure null
    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        UserDetails userDetails = getUser();
        if (userDetails == null) {
            return null;
        }
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        return credentials == null ? null : credentials.getUser();
    }
}

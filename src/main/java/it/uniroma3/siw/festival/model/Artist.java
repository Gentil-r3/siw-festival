package it.uniroma3.siw.festival.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity 
public class Artist {
    @Id 
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id ; 

    @NotBlank 
    @Column (nullable = false)
    private String name ;   

    @NotBlank 
    @Column (nullable = false)  
    private String surname ;

    @NotNull 
    @Column (nullable = false)
    private LocalDate dateOfBirth ;

    @NotBlank 
    @Column (nullable = false)
    private String nationality ;

    @JsonIgnore // non va nel JSON: eviterebbe il ciclo artista -> film -> regista -> film... (lez. 11 slide 26)
    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movie> directedMovies;

    @JsonIgnore
    @OneToMany(mappedBy = "artist") // i ruoli interpretati come attore
    private List<Interpretation> interpretations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Movie> getDirectedMovies() {
        return directedMovies;
    }

    public void setDirectedMovies(List<Movie> directedMovies) {
        this.directedMovies = directedMovies;
    }

    public List<Interpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    @Override 
    public int hashCode(){
        return Objects.hash(id);
    }
    
    @Override 
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Artist artist = (Artist) obj;
        return Objects.equals(id, artist.id);
    }
}

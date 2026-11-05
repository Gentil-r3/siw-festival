package it.uniroma3.siw.festival.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Interpretation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String characterName;   // il personaggio interpretato

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;            // il film
                                    // un interpetazione deve avere un film e un attore 
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;          // l'attore

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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
        Interpretation interpretation = (Interpretation) obj;
        return Objects.equals(id, interpretation.id);
    }
}

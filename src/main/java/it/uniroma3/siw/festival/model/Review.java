package it.uniroma3.siw.festival.model;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
// al massimo una recensione per utente per film, garantito anche dal DB (lez. 04 slide 54)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"}))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String text;           // il testo della recensione

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;        // voto da 1 a 5

    @Column(nullable = false)
    private LocalDate date;        // data della recensione: la imposta il service, non arriva dalla form

    @JsonIgnore // le recensioni si chiedono già per film (/api/movies/{id}/reviews): il film non serve
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;           // il film recensito

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;             // l'autore della recensione

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Review review = (Review) obj;
        return Objects.equals(id, review.id);
    }
}

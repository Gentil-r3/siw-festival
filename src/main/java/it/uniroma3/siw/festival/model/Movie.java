package it.uniroma3.siw.festival.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.uniroma3.siw.festival.validation.NotFutureYear;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String title;

	@NotNull
	@Min(1888)
	@NotFutureYear // vincolo custom (lez. 08 slide 30-33)
	@Column(nullable = false)
	private Integer year;

	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer duration;      // in minuti

	@NotBlank
	@Column(nullable = false)
	private String genre;

	@NotBlank
	@Column(nullable = false)
	private String productionCountry;

	@ManyToOne(fetch = FetchType.LAZY) // un film ha solo un regista; LAZY: caricato solo quando serve
	@JoinColumn(name = "director_id")
	private Artist director;

	@JsonIgnore // collezioni fuori dal JSON: evitano cicli infiniti (lez. 11 slide 26)
	@ManyToMany(mappedBy = "movies") // i festival in cui è presente il film
	private List<Festival> festivals;

	@JsonIgnore
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true) // gli attori con il loro personaggio
	private List<Interpretation> interpretations;

	@JsonIgnore
	@OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE) // le proiezioni del film; REMOVE: se cancello il film si cancellano anche le sue proiezioni
	private List<Screening> screenings;

	@JsonIgnore // le recensioni si leggono con GET /api/movies/{id}/reviews
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true) // le recensioni del film
	private List<Review> reviews;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public Artist getDirector() {
        return director;
    }

    public void setDirector(Artist director) {
        this.director = director;
    }

    public List<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }

    public List<Interpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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
        Movie movie = (Movie) obj;
        return Objects.equals(id, movie.id);
    }
}

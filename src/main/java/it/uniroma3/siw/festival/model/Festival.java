package it.uniroma3.siw.festival.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
//ENTITÀ
@Entity
public class Festival {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@NotNull
	@Column(nullable = false)
	private Integer year;

	@NotBlank
	@Column(nullable = false)
	private String city;

	@NotNull
	@Column(nullable = false)
	private LocalDate startDate;

	@NotNull
	@Column(nullable = false)
	private LocalDate endDate;

	private String description;

	@JsonIgnore // i film si leggono con GET /api/festivals/{id}/movies
	@ManyToMany
	@JoinTable(
		name = "festival_movie",
		joinColumns = @JoinColumn(name = "festival_id"),
		inverseJoinColumns = @JoinColumn(name = "movie_id")
	)
	private List<Movie> movies;

	@JsonIgnore // le proiezioni si leggono con GET /api/festivals/{id}/screenings
	@OneToMany(mappedBy = "festival") // le proiezioni del festival
	private List<Screening> screenings;

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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	public List<Screening> getScreenings() {
		return screenings;
	}

	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Festival other = (Festival) obj;
		return Objects.equals(id, other.id);
	}
}

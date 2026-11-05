package it.uniroma3.siw.festival;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import it.uniroma3.siw.festival.model.Festival;
import it.uniroma3.siw.festival.model.Movie;
import it.uniroma3.siw.festival.repository.FestivalRepository;
import it.uniroma3.siw.festival.repository.MovieRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
public class FestivalApplication implements CommandLineRunner {

	private final FestivalRepository festivalRepository;
	private final MovieRepository movieRepository;
	private final EntityManager entityManager;
	private final Statistics statistics;

	public FestivalApplication(FestivalRepository festivalRepository, MovieRepository movieRepository,
			EntityManager entityManager, EntityManagerFactory entityManagerFactory) {
		this.festivalRepository = festivalRepository;
		this.movieRepository = movieRepository;
		this.entityManager = entityManager;
		this.statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
	}

	public static void main(String[] args) {
		SpringApplication.run(FestivalApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Festival festival = festivalRepository.findByName("Mostra del Cinema di Venezia").get();
		System.out.println("=== Test accesso ai film del festival con i registi ===");
		strategyLazy(festival.getId());
		strategyJoinFetch(festival.getId());
		strategyEntityGraph(festival.getId());
	}

	private void strategyLazy(Long festivalId) {
		start();
		StopWatch watch = new StopWatch();
		watch.start();
		List<Movie> movies = movieRepository.findByFestivalsId(festivalId);
		readDirectors(movies);
		watch.stop();
		print("Strategia 1: LAZY", movies, watch);
	}

	private void strategyJoinFetch(Long festivalId) {
		start();
		StopWatch watch = new StopWatch();
		watch.start();
		List<Movie> movies = movieRepository.findByFestivalWithDirector(festivalId);
		readDirectors(movies);
		watch.stop();
		print("Strategia 2: JOIN FETCH", movies, watch);
	}

	private void strategyEntityGraph(Long festivalId) {
		start();
		StopWatch watch = new StopWatch();
		watch.start();
		List<Movie> movies = movieRepository.findWithDirectorByFestivalsId(festivalId);
		readDirectors(movies);
		watch.stop();
		print("Strategia 3: ENTITY GRAPH", movies, watch);
	}

	private void start() {
		entityManager.clear();
		statistics.clear();
	}

	private void readDirectors(List<Movie> movies) {
		for (Movie movie : movies) {
			if (movie.getDirector() != null) {
				movie.getDirector().getSurname();
			}
		}
	}

	private void print(String strategy, List<Movie> movies, StopWatch watch) {
		System.out.println(strategy);
		System.out.println("Film caricati: " + movies.size());
		System.out.println("Oggetti caricati: " + statistics.getEntityLoadCount());
		System.out.println("Query SQL: " + statistics.getPrepareStatementCount());
		System.out.println("Tempo: " + watch.getTotalTimeMillis() + " ms");
	}
}

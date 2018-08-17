package ru.homework.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ru.homework.domain.Genre;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(connection=EmbeddedDatabaseConnection.H2)
public class GenreRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private GenreRepository genreRepository;	

	@Test
    @Rollback(true)	
	public void isH2Test() {
		Optional<Genre> dbGenre = genreRepository.findById(1000);
		assertEquals(dbGenre.get().getName(), "Тестовый жанр");
	}		
	
	@Test
    @Rollback(true)	
	public void testInsert() {
		Genre testGenre = new Genre("Изобразительное искусство");
		this.entityManager.persist(testGenre);
		Genre dbGenre = genreRepository.findById(testGenre.getId()).orElse(null);
		assertEquals(testGenre, dbGenre);
	}		
	
	@Test
    @Rollback(true)	
	public void testFindAllByFilters() {
		Genre genre1 = new Genre("Биографии и мемуары"); 
		genreRepository.save(genre1);
		Genre genre2 = new Genre("Изобразительное искусство и фотография");
		genreRepository.save(genre2);
		HashMap<String, String> filters = new HashMap<>();
		filters.put("name", "граф");
		int count = (int) genreRepository.count();
		List<Genre> genres = new ArrayList<>();
		genreRepository.findAllByFilters(filters, PageRequest.of(0, count)).forEach(genres::add);
		assertTrue(genres.contains(genre1));
		assertTrue(genres.contains(genre2));
	}
	
}

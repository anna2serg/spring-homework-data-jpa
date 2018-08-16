package ru.homework.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Genre;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"test"})
public class GenreRepositoryTest {
	
	@Autowired
	private GenreRepository genreRepository;	

	@Test
    @Transactional
    @Rollback(true)	
	public void isH2Test() {
		Optional<Genre> dbGenre = genreRepository.findById(1000);
		assertEquals(dbGenre.get().getName(), "Тестовый жанр");
	}		
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testInsert() {
		Genre testGenre = new Genre("Изобразительное искусство");
		genreRepository.save(testGenre);
		Genre dbGenre = genreRepository.findById(testGenre.getId()).orElse(null);
		assertEquals(testGenre, dbGenre);
	}		
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testUpdate() {
		Genre testGenre = new Genre("Изобразительное искусство");
		genreRepository.save(testGenre);		
		testGenre.setName("Изобразительное искусство и фотография");
		genreRepository.save(testGenre);
		Genre dbGenre = genreRepository.findById(testGenre.getId()).orElse(null);
		assertFalse(dbGenre == null);
		assertEquals(testGenre.getName(), dbGenre.getName());	
		dbGenre = genreRepository.findByName("Изобразительное искусство и фотография").orElse(null);
		assertEquals(testGenre, dbGenre);		
	}		
		
	@Test
    @Transactional
    @Rollback(true)	
	public void testDelete() {
		Genre testGenre = new Genre("Изобразительное искусство");
		genreRepository.save(testGenre);
		int testGenreId = testGenre.getId();
		genreRepository.delete(testGenre);
		Optional<Genre> dbGenre = genreRepository.findById(testGenreId);
		assertEquals(dbGenre, Optional.empty());
	}		
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testCount() {
		long count = genreRepository.count();
		Genre testGenre = new Genre("Изобразительное искусство");
		genreRepository.save(testGenre);
		assertTrue(genreRepository.count() == (count + 1));
	}	
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testGetAll() {
		Genre genre1 = new Genre("Биографии и мемуары"); 
		genreRepository.save(genre1);
		Genre genre2 = new Genre("Изобразительное искусство и фотография");
		genreRepository.save(genre2);
		HashMap<String, String> filters = new HashMap<>();
		filters.put("name", "граф");
		int count = (int) genreRepository.count();
		List<Genre> genres = new ArrayList<>();
		genreRepository.findAll(PageRequest.of(0, count)).forEach(genres::add);;
		assertTrue(genres.contains(genre1));
		assertTrue(genres.contains(genre2));
	}
	
}

package ru.homework.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Author;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(connection=EmbeddedDatabaseConnection.H2)
public class AuthorRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;	
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testInsert() {
		Author testAuthor = new Author("Достоевский", "Федор", "Михайлович");
		this.entityManager.persist(testAuthor);
		Author dbAuthor = authorRepository.findById(testAuthor.getId()).orElse(null);
		assertEquals(testAuthor, dbAuthor);		
	}

	@Test
    @Transactional
    @Rollback(true)	
	public void testFindAllByFilters() {
		Author author1 = new Author("Салтыков-Щедрин", "Михаил", "Евграфович");
		authorRepository.save(author1);
		Author author2 = new Author("Достоевский", "Федя", "Михайлович");
		authorRepository.save(author2);		
		HashMap<String, String> filters = new HashMap<>();
		filters.put("name", "миха");
		int count = (int) authorRepository.count();
		List<Author> authors = new ArrayList<>();
		authorRepository.findAllByFilters(filters, PageRequest.of(0, count)).forEach(authors::add);
		assertTrue(authors.contains(author1));
		assertTrue(authors.contains(author2));			
	}	

}

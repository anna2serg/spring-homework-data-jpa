package ru.homework.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ru.homework.domain.Book;
import ru.homework.domain.Genre;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(connection=EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;	
	
	@Autowired
	private BookRepository bookRepository;	

	private Book getTestBook1() {
		Set<Author> testBookAuthors = new HashSet<>();
		testBookAuthors.add(new Author(14, "Агафонов", "А", "В"));
		testBookAuthors.add(new Author(15, "Пожарская", "Светлана", "Георгиевна"));
		Genre testBookGenre = new Genre(3, "Детская литература");
		Book testBook = new Book("Фотобукварь", testBookAuthors, testBookGenre);
		this.entityManager.persist(testBook);	
		Book dbBook = bookRepository.findById(testBook.getId()).orElse(null);
		assertEquals(testBook, dbBook);			
		return dbBook;
	}
	
	private Book getTestBook2() {	
		Set<Author> testBookAuthors = new HashSet<>();
		testBookAuthors.add(new Author(16, "Ткаченко", "Наталия", "Александровна"));
		testBookAuthors.add(new Author(17, "Тумановская", "Мария", "Петровна"));
		Genre testBookGenre = new Genre(3, "Детская литература");
		Book testBook = new Book("Букварь для малышей", testBookAuthors, testBookGenre);
		this.entityManager.persist(testBook);
		Book dbBook = bookRepository.findById(testBook.getId()).orElse(null);
		assertEquals(testBook, dbBook);			
		return dbBook;
	}	
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testInsert() {
		getTestBook1();
	}

	@Test
    @Transactional
    @Rollback(true)	
	public void testFindAllByFilters() {
		Book book1 = getTestBook1();
		Book book2 = getTestBook2();
		HashMap<String, String> filters = new HashMap<>();
		filters.put("name", "букварь");
		int count = (int) bookRepository.count();
		List<Book> books = new ArrayList<>();
		bookRepository.findAllByFilters(filters, PageRequest.of(0, count)).forEach(books::add);
		assertTrue(books.contains(book1));
		assertTrue(books.contains(book2));				
	}

}

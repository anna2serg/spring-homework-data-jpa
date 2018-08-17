package ru.homework.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Book;
import ru.homework.domain.Comment;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(connection=EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;	
	
	@Autowired
	private CommentRepository commentRepository;		

	private Book getTestBook() {
		HashMap<String, String> filters = new HashMap<String, String>(); 
		Page<Comment> comments = commentRepository.findAllByFilters(filters, PageRequest.of(0, 1));
		return comments.iterator().next().getBook();
	}
	
	@Test
    @Transactional
    @Rollback(true)	
	public void testInsert() {
		Comment testComment = new Comment(getTestBook(), (short) 4, "Бу", "Каспер");
		this.entityManager.persist(testComment);
		Comment dbComment = commentRepository.findById(testComment.getId()).orElse(null);
		assertEquals(testComment, dbComment);	
	}

	@Test
    @Transactional
    @Rollback(true)	
	public void testFindAllByFilters() {
		Comment testComment = new Comment(getTestBook(), (short) 4, "Бу", "Каспер");
		commentRepository.save(testComment);		
		HashMap<String, String> filters = new HashMap<String, String>(); 
		filters.put("commentator", "Каспер");
		int count = (int) commentRepository.count();
		List<Comment> comments = new ArrayList<>();
		commentRepository.findAllByFilters(filters, PageRequest.of(0, count)).forEach(comments::add);		
		assertTrue(comments.contains(testComment));
	}

}

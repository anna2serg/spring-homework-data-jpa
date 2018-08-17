package ru.homework.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.core.BooleanBuilder;

import ru.homework.domain.Book;
import ru.homework.domain.QBook;
import ru.homework.helper.Helper;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer>, QuerydslPredicateExecutor<Book> {

	List<Book> findByName(String name);
	
	static BooleanBuilder bookBuilder(HashMap<String, String> filters) {
		
		QBook gBook = QBook.book;
		BooleanBuilder builder = new BooleanBuilder();
		
		if (filters.get("name")!= null && !filters.get("name").isEmpty()) {	
			builder.and(gBook.name.toLowerCase().like("%"+filters.get("name").toLowerCase()+"%"));
		}
		if (filters.get("author")!= null && !filters.get("author").isEmpty()) {
			String author = "%"+filters.get("author").toLowerCase()+"%";		
			builder.and(gBook.authors.any().firstname.toLowerCase().like(author)
				    .or(gBook.authors.any().surname.toLowerCase().like(author))
				    .or(gBook.authors.any().middlename.toLowerCase().like(author)));
		}
		if (filters.get("genre")!= null && !filters.get("genre").isEmpty()) {	
			builder.and(gBook.genre.name.toLowerCase().like("%"+filters.get("genre").toLowerCase()+"%"));
		}	
		if (filters.get("author_id")!= null && !filters.get("author_id").isEmpty()) {
			int author_id = Helper.getInt(filters.get("author_id"));
			if (author_id!=-1) 
				builder.and(gBook.authors.any().id.eq(author_id));
		}	
		if (filters.get("genre_id")!= null && !filters.get("genre_id").isEmpty()) {	
			int genre_id = Helper.getInt(filters.get("genre_id"));
			if (genre_id!=-1)
				builder.and(gBook.genre.id.eq(genre_id));
		}		
		
		return builder;
	}	
	
	default public int getBookCount(HashMap<String, String> filters) {
		List<Book> result = new ArrayList<>();
		findAll(bookBuilder(filters)).forEach(result::add);
		return result.size();
	}	
	
	default public Page<Book> findAllByFilters(HashMap<String, String> filters, Pageable pageable) {		
		return (Page<Book>) findAll(bookBuilder(filters), pageable);
	}
	
}

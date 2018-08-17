package ru.homework.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.core.BooleanBuilder;

import ru.homework.domain.Author;
import ru.homework.domain.QAuthor;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer>, QuerydslPredicateExecutor<Author> {
	
	List<Author> findBySurnameAndFirstnameAndMiddlename(String surname, String firstname, String middlename);
	
	@Query("select a from Author a "
		 + "where lower(a.surname) like %?1% "
	   	 + "or lower(a.firstname) like %?1% "
	  	 + "or lower(a.middlename) like %?1% ")
	Page<Author> findByNameLike(String name, Pageable pageable);
	
	static BooleanBuilder authorBuilder(HashMap<String, String> filters) {
		
		QAuthor qAuthor = QAuthor.author;
		BooleanBuilder builder = new BooleanBuilder();
				
		if (filters.get("name")!= null && !filters.get("name").isEmpty()) {		
			String author = "%"+filters.get("name").toLowerCase()+"%";		
			builder.and(qAuthor.firstname.toLowerCase().like(author)
				    .or(qAuthor.surname.toLowerCase().like(author))
				    .or(qAuthor.middlename.toLowerCase().like(author)));
		}	
		
		return builder;
	}			
	
	default public Page<Author> findAllByFilters(HashMap<String, String> filters, Pageable pageable) {		
		return (Page<Author>) findAll(authorBuilder(filters), pageable);
	}	
	
}

package ru.homework.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer>, QuerydslPredicateExecutor<Author> {
	
	List<Author> findBySurnameAndFirstnameAndMiddlename(String surname, String firstname, String middlename);
	
	@Query("select a from Author a "
		 + "where lower(a.surname) like %?1% "
	   	 + "or lower(a.firstname) like %?1% "
	  	 + "or lower(a.middlename) like %?1% ")
	Page<Author> findByNameLike(String name, Pageable pageable);
	
}

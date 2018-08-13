package ru.homework.repository;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer>, QuerydslPredicateExecutor<Book> {
	
	List<Book> findByName(String name);
	
}

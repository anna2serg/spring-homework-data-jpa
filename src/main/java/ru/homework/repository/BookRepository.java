package ru.homework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
	
	List<Book> findByName(String name);
	
}

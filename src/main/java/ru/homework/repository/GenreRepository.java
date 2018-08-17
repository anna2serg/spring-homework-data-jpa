package ru.homework.repository;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.core.BooleanBuilder;

import ru.homework.domain.Genre;
import ru.homework.domain.QGenre;

public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer>, QuerydslPredicateExecutor<Genre> {
	
	Optional<Genre> findByName(String name);
	
	static BooleanBuilder genreBuilder(HashMap<String, String> filters) {
		
		QGenre qGenre = QGenre.genre; 
		BooleanBuilder builder = new BooleanBuilder();
		
		if (filters.get("name")!= null && !filters.get("name").isEmpty()) {		
			builder.and(qGenre.name.toLowerCase().like("%"+filters.get("name").toLowerCase()+"%"));
		}	
		
		return builder;
	}		
	
	default public Page<Genre> findAllByFilters(HashMap<String, String> filters, Pageable pageable) {		
		return (Page<Genre>) findAll(genreBuilder(filters), pageable);
	}	
	
}

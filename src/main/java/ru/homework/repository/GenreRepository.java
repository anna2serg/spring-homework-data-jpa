package ru.homework.repository;

import java.util.Optional;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Genre;

public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer>, QuerydslPredicateExecutor<Genre> {
	
	Optional<Genre> findByName(String name);
	
}

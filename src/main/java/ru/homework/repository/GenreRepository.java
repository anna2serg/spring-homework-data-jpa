package ru.homework.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Genre;

public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer> {
	
	List<Genre> findAll();
	Optional<Genre> findByName(String name);
	List<Genre> findByNameLike(String name);
	
}

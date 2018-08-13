package ru.homework.repository;

import java.util.HashMap;
import java.util.List;

import ru.homework.domain.Book;

public interface BookRepositoryCustom {
	List<Book> findAll(HashMap<String, String> filters);
}

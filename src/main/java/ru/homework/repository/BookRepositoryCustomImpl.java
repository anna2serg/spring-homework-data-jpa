package ru.homework.repository;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Book;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {
	
   @PersistenceContext
   private EntityManager em;
	   
	@Override
	@Transactional
	public List<Book> findAll(HashMap<String, String> filters) {
		// TODO Auto-generated method stub
		return null;
	}

}

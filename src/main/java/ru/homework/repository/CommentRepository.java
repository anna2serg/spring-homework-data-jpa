package ru.homework.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.homework.domain.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer>, QuerydslPredicateExecutor<Comment> {
	
}
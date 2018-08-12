package ru.homework.repository;

import java.util.HashMap;
import java.util.List;

import ru.homework.domain.Comment;

public interface CommentRepository {
	
	int count();
	int insert(Comment comment);
	Comment getById(int id);
	List<Comment> getAll(HashMap<String, String> filters);
}
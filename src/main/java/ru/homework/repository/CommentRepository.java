package ru.homework.repository;

import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.core.BooleanBuilder;

import ru.homework.domain.Comment;
import ru.homework.domain.QComment;
import ru.homework.helper.Helper;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer>, QuerydslPredicateExecutor<Comment> {
	
	static BooleanBuilder commentBuilder(HashMap<String, String> filters) {
		
		QComment qComment = QComment.comment;

		BooleanBuilder builder = new BooleanBuilder();
		
		if (filters.get("commentator")!= null && !filters.get("commentator").isEmpty()) {	
			builder.and(qComment.commentator.toLowerCase().like("%"+filters.get("commentator").toLowerCase()+"%"));
		}
		if (filters.get("book")!= null && !filters.get("book").isEmpty()) {
			builder.and(qComment.book.name.toLowerCase().like("%"+filters.get("book").toLowerCase()+"%"));
		}
		if (filters.get("author")!= null && !filters.get("author").isEmpty()) {
			String author = "%"+filters.get("author").toLowerCase()+"%";		
			builder.and(qComment.book.authors.any().firstname.toLowerCase().like(author)
				    .or(qComment.book.authors.any().surname.toLowerCase().like(author))
				    .or(qComment.book.authors.any().middlename.toLowerCase().like(author)));
		}
		if (filters.get("book_id")!= null && !filters.get("book_id").isEmpty()) {	
			int book_id = Helper.getInt(filters.get("book_id"));
			if (book_id!=-1)
				builder.and(qComment.book.id.eq(book_id));
		}		
		if (filters.get("author_id")!= null && !filters.get("author_id").isEmpty()) {
			int author_id = Helper.getInt(filters.get("author_id"));
			if (author_id!=-1) 
				builder.and(qComment.book.authors.any().id.eq(author_id));
		}		
		
		return builder;
	}
	
	default public Page<Comment> findAllByFilters(HashMap<String, String> filters, Pageable pageable) {		
		return (Page<Comment>) findAll(commentBuilder(filters), pageable);
	}	
	
}
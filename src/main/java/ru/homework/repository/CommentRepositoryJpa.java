package ru.homework.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Author;
import ru.homework.domain.Book;
import ru.homework.domain.Comment;

@Transactional
@Repository
public class CommentRepositoryJpa implements CommentRepository {
	
    @PersistenceContext
    private EntityManager em;		
	
	@Override
	public int count() {
		TypedQuery<Long> query = em.createQuery("select count(c.id) from Comment c", Long.class);
        return query.getSingleResult().intValue(); 	
	}    
    
	@Override
	public int insert(Comment comment) {
		em.persist(comment);
		return comment.getId();
	}

	@Override
	public List<Comment> getAll(HashMap<String, String> filters) {
	
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Comment> commentCriteria = cb.createQuery(Comment.class);
		Root<Comment> commentRoot = commentCriteria.from(Comment.class);
		Join<Comment, Book> withBookJoin = commentRoot.join("book");
		Join<Comment, Author> withAuthorJoin = withBookJoin.join("authors");
		
		if (filters.keySet().size() > 0) {
			
			final List<Predicate> predicates = new ArrayList<Predicate>();		
			for (final Entry<String, String> filter : filters.entrySet()) {
				final String key = filter.getKey();
			    final String value = filter.getValue().trim().toLowerCase();
			    
			    if ((key != null) && (value != null)) {
			    	
		            switch (key) {
		    	        case "commentator":
		    	        	predicates.add(cb.like(cb.lower(commentRoot.get(key)), "%" + value + "%"));
		    	            break;
		    	        case "book":
		    	        	predicates.add(cb.like(cb.lower(withBookJoin.get("name")), "%" + value + "%"));
		    	            break;
		    	        case "author":	    	        		
		    	        	Predicate authorSurname = cb.like(cb.lower(withAuthorJoin.get("surname")), "%" + value + "%");
		    	        	Predicate authorFirstname = cb.like(cb.lower(withAuthorJoin.get("firstname")), "%" + value + "%");
		    	        	Predicate authorMiddlename = cb.like(cb.lower(withAuthorJoin.get("middlename")), "%" + value + "%");
		    	        	Predicate byAuthor = cb.or(authorSurname, authorFirstname, authorMiddlename);
		    	        	predicates.add(byAuthor);
		    	            break;
		    	        case "book_id":
		    	        	predicates.add(cb.equal(withBookJoin.get("id"), value));
		    	            break;
		    	        case "author_id":
		    	        	predicates.add(cb.equal(withAuthorJoin.get("id"), value));
		    	            break;	    	            
		    	        default:         
		            }				    	
	            
			    }			
			}
			
			commentCriteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));		
			
		} else {
			commentCriteria.select(commentRoot);
		}
		
		//distinct + orderBy != love
		//commentCriteria.distinct(true);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(withBookJoin.get("id")));
		orderList.add(cb.asc(commentRoot.get("id")));	
		commentCriteria.orderBy(orderList); 
		
		List<Comment> commentsWithDoubles = em.createQuery(commentCriteria).getResultList();
		List<Comment> result = new ArrayList<Comment>(new HashSet<>(commentsWithDoubles));
		
		return result;
	}

	@Override
	public Comment getById(int id) {
		return em.find(Comment.class, id);
	}

}

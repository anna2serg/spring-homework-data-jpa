package ru.homework.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ru.homework.helper.Helper;

@Entity
@Table(name = "comments")
public class Comment {
    private int id;
    private Book book;    
    private String commentator;
    private String content;
    private short score;
    
    public Comment() {
        super();
    } 

    public Comment(Book book, short score, String content, String commentator) {
        this(0, book, score, content, commentator);
    }       
    
    public Comment(int id, Book book, short score, String content, String commentator) {
        super();
        this.id = id;
        this.book = book;
        if (commentator == null || commentator.isEmpty()) {
        	this.commentator = "Anonym";
        } else {
        	this.commentator = commentator;
        }
        this.content = content;
        this.score = score;
    }        

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_generator")  
    @SequenceGenerator(name="comment_generator", sequenceName = "comment_seq", initialValue = 100, allocationSize=1)
    @Column(name = "comment_id")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name="book_id")
    public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }    
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public short getScore() {
		return score;
	}

	public void setScore(short score) {
		this.score = score;
	}

	@Override
    public String toString() {
    	String result = "";
    	result += String.format("%-10s", this.id);
    	result += "| ";     	
    	result += String.format("%-25s", Helper.ellipsize(this.commentator, 25));
    	result += "| "; 
    	result += String.format("%-3s", this.getScore());
    	result += "| "; 
    	result += String.format("%-50s", Helper.ellipsize(this.content, 50));
    	result += "| ";     	
    	result += String.format("%-50s", Helper.ellipsize(this.book.toString(), 50));
		return result;
    }

	@Override
	public int hashCode() {
		return java.util.Objects.hash(getId(), getBook(), getCommentator(), getContent(), getScore());
	}

	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }  	
        if (!(obj instanceof Comment)) {
            return false;
        }       
        Comment other = (Comment)obj;
        
        return (Objects.equals(other.id, this.id)) && 
        	   (Objects.equals(other.commentator, this.commentator)) && 
			   (Objects.equals(other.content, this.content)) &&
			   (Objects.equals(other.score, this.score)) &&
			   (Objects.equals(other.book, this.book));    
	}    
     
}

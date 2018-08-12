package ru.homework.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import ru.homework.helper.StringHelper;

@Entity
@Table(name = "books")
public class Book {
    private int id;
    private String name;
    private Set<Author> authors = new HashSet<Author>();
    private Genre genre;
    private Set<Comment> comments = new HashSet<Comment>();
    private float score;
  
    public Book() {
    	super();
    }       
    
    public Book(String name, Set<Author> authors, Genre genre) {
        this(0, name, authors, genre);
    }            
 
    public Book(int id, String name, Set<Author> authors, Genre genre) {
        super();
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.genre = genre;
    }        

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")  
    @SequenceGenerator(name="book_generator", sequenceName = "book_seq", initialValue = 100, allocationSize=1)    
    @Column(name = "book_id")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "books_authors", 
            joinColumns = { @JoinColumn(name = "book_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "author_id") }
        )    
    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
    	this.authors = authors;
    } 
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }      
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="genre_id")
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }    
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Transient
	public float getScore() {
		score = 0;
		for (Comment comment : comments) {
			score += comment.getScore();
		}
		if (comments.size() > 0) { 
			score = score / comments.size();
			String str = String.format(java.util.Locale.US, "%.2f", score);
			score = Float.parseFloat(str); 
		}
		return score;
	}

	@Override
    public String toString() {
    	String result = "";
    	String book_name = String.format("[%s] %s", id, name);
    	book_name = StringHelper.ellipsize(book_name, 50);
    	result += String.format("%-50s", book_name);
    	result += "| "; 
    	String book_genre = StringHelper.ellipsize(genre.toString(), 25);
    	result += String.format("%-25s", book_genre);
    	result += "| ";
    	boolean isFirstAuthor = true;
    	for (Author author : authors) {
    		if (isFirstAuthor) {
    			result += String.format("%-35s", author);
    			result += "| ";
    			result += String.format("%-10s\r\n", getScore());
    		} else {
            	result += String.format("%-50s", "");
            	result += "| ";
            	result += String.format("%-25s", "");
            	result += "| ";
            	result += String.format("%-35s", author); 
            	result += "| ";
            	result += String.format("%-10s\r\n", "");	
    		}
    		isFirstAuthor = false;	
    	}

        return result; 
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }  	
        if (!(obj instanceof Book)) {
            return false;
        }       
        Book other = (Book)obj;
        
        return (Objects.equals(other.id, this.id)) && 
        	   (Objects.equals(other.name, this.name)) && 
			   (Objects.equals(other.genre, this.genre)) &&
			   (Objects.equals(other.authors, this.authors))
			   //(other.authors.size() == this.authors.size() &&
			   //(other.authors.containsAll(this.authors)) &&
			   //(this.authors.containsAll(other.authors)))
			   ;    
    }  
    
}

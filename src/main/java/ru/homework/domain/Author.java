package ru.homework.domain;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {
    private int id;
    private String surname;
    private String firstname;
    private String middlename;
    
    private Set<Book> books;
    
    public Author() {
        super();
    } 
    
    public Author(String surname, String firstname, String middlename) {
        this(0, surname, firstname, middlename);
    }     
    
    public Author(int id, String surname, String firstname, String middlename) {
        super();
        this.id = id;
        this.surname = surname;
        this.firstname = firstname;
        this.middlename = middlename;
    }        

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")  
    @SequenceGenerator(name="author_generator", sequenceName = "author_seq", initialValue = 100, allocationSize=1) 
    @Column(name = "author_id")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }    
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }        
    
    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }       
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authors")
    public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
    public String toString() {
        return String.format("[%s] %s %s %s", id, surname, firstname, (middlename != null && !middlename.isEmpty())?middlename:"");
    }

	@Override
	public int hashCode() {
		return java.util.Objects.hash(getId(), getSurname(), getFirstname(), getMiddlename());
	}
   
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }  	
        if (!(obj instanceof Author)) {
            return false;
        }       
        Author other = (Author)obj;
         
        return ( (Objects.equals(other.id, this.id)) && 
        		 (Objects.equals(other.firstname, this.firstname)) && 
        		 (Objects.equals(other.surname, this.surname)) &&
        		 (Objects.equals(other.middlename, this.middlename))
        	   );
    }
       
}

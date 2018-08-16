package ru.homework.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "genres")
public class Genre {
    private int id;
    private String name;
    
    public Genre() {
        super();
    } 

    public Genre(String name) {
        this(0, name);
    }       
    
    public Genre(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }        

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_generator")  
    @SequenceGenerator(name="genre_generator", sequenceName = "genre_seq", initialValue = 100, allocationSize=1)
    @Column(name = "genre_id")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
    @Override
    public String toString() {
        return String.format("[%s] %s", id, name);
    }

	@Override
	public int hashCode() {
		return java.util.Objects.hash(getId(),getName());
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }  	
        if (!(obj instanceof Genre)) {
            return false;
        }       
        Genre other = (Genre)obj;
        
        return ( (Objects.equals(other.id, this.id)) && 
        		 (Objects.equals(other.name, this.name)) );
    }  
    
}

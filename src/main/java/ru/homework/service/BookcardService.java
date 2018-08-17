package ru.homework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homework.domain.Author;
import ru.homework.domain.Book;
import ru.homework.domain.Comment;
import ru.homework.domain.Genre;
import ru.homework.exception.EntityNotFoundException;
import ru.homework.exception.InvalidOperationException;
import ru.homework.exception.InvalidValueFormatException;
import ru.homework.exception.NotUniqueEntityFoundException;
import ru.homework.helper.Helper;
import ru.homework.repository.AuthorRepository;
import ru.homework.repository.BookRepository;
import ru.homework.repository.CommentRepository;
import ru.homework.repository.GenreRepository;

@Service
public class BookcardService {
	
	private final BookRepository bookRepository;
	private final GenreRepository genreRepository;
	private final AuthorRepository authorRepository;
	private final CommentRepository commentRepository;
	private final FetchDataService fetcher;
	
	public BookcardService(AuthorRepository authorRepostory, BookRepository bookRepostory, GenreRepository genreRepostory, CommentRepository commentRepostory, FetchDataService fetcher) {
		this.authorRepository = authorRepostory;
		this.bookRepository = bookRepostory;
		this.genreRepository = genreRepostory;
		this.commentRepository = commentRepostory;
		this.fetcher = fetcher;
	}

	public Genre getGenre(String genre) throws EntityNotFoundException {
		Genre result = null;
		int genre_id = Helper.getInt(genre);		
		if (genre_id == -1) {
			//genre - строка
			result = genreRepository.findByName(genre).orElse(null);				
		} else {
			//genre - число 
			result = genreRepository.findById(genre_id).orElse(null); 
		}		
		if (result == null) 
			throw new EntityNotFoundException(String.format("Жанр [%s] не найден", genre));		
		return result;
	}	

	public void getGenreAll(HashMap<String, String> filters) {
		fetcher.print(genreRepository::findAllByFilters, 
					  filters, 
					  Sort.by("name").ascending());
	}	
	
	@Transactional 
	public Genre addGenre(String name) {
		Genre result = null;
		result = new Genre(name);
		genreRepository.save(result);
		return result;
	}
	
	@Transactional 
	public Genre editGenre(String genre, String name) throws EntityNotFoundException {
		Genre result = getGenre(genre);
		result.setName(name);
		genreRepository.save(result);
		return result;
	}	
	
	private List<String> getAuthorNames(String str) throws InvalidValueFormatException {
		String[] names = str.split(",");			
		if (names.length < 2)  
			throw new InvalidValueFormatException(String.format("Неправильно задан автор [%s]", str));
		List<String> result = new ArrayList<String>(); 
		for (String name : names) {
			result.add(name.trim());
		}		
		return result;
	}
	
	public Author getAuthor(String author) throws EntityNotFoundException, NotUniqueEntityFoundException {
		List<Author> authors = getAuthors(author);
		if (authors.size()>1) 
			throw new NotUniqueEntityFoundException(String.format("Найдено более одного автора [%s]", author));				
		return authors.get(0);
	}
	
	public List<Author> getAuthors(String author) throws EntityNotFoundException {
		List<Author> result = null;
		int author_id = Helper.getInt(author);
		if (author_id == -1) {
			//author - строка
			List<String> names;
			try {
				names = getAuthorNames(author);
			} catch (InvalidValueFormatException e) {
				throw new EntityNotFoundException(String.format("Автор [%s] не найден: %s", author, e.getMessage()));
			}
			result = authorRepository.findBySurnameAndFirstnameAndMiddlename(names.get(0), names.get(1), (names.size() > 2) ? names.get(2) : null);			
		} else {
			//author - число 
			Author authorById = authorRepository.findById(author_id).orElse(null);
			if (authorById!=null) {
				result = new ArrayList<Author>();
				result.add(authorById);				
			}			
		}
		if ((result == null) || (result.size() == 0)) 
			throw new EntityNotFoundException(String.format("Автор [%s] не найден", author));
		return result;
	}		
		
	public void getAuthorAll(HashMap<String, String> filters) {
		fetcher.print(authorRepository::findAllByFilters, 
					  filters, 
					  Sort.by("surname").ascending().and(Sort.by("firstname").ascending()));  		
	}		
	
	@Transactional 
	public Author addAuthor(String surname, String firstname, String middlename) {
		Author result = null;
		result = new Author(surname, firstname, middlename);
		authorRepository.save(result);
		return result;		
	}	
	
	@Transactional 
	public Author editAuthor(String author, HashMap<String, String> values) throws EntityNotFoundException, NotUniqueEntityFoundException {
		Author result = getAuthor(author);	
		if (values.get("surname")!=null) 
			result.setSurname(values.get("surname"));
		if (values.get("firstname")!=null)
			result.setFirstname(values.get("firstname"));
		if (values.get("middlename")!=null)
			if (values.get("middlename").equals("null")) result.setMiddlename(null); 
			else result.setMiddlename(values.get("middlename"));		
		authorRepository.save(result);
		return result;		
	}		
	
	public void getBookAll(HashMap<String, String> filters) {
		fetcher.print(bookRepository::findAllByFilters, 
					  filters, 
					  Sort.by("name").ascending());
	}	
	
	public Book getBook(String book) throws EntityNotFoundException, NotUniqueEntityFoundException {		
		List<Book> books =  getBooks(book);
		if (books.size()>1) 
			throw new NotUniqueEntityFoundException(String.format("Найдено более одной книги [%s]", book));	
		return books.get(0);
	}	
	
	public List<Book> getBooks(String book) throws EntityNotFoundException {
		List<Book> result = null;
		int book_id = Helper.getInt(book);
		if (book_id == -1) {
			//book - строка
			result = bookRepository.findByName(book);
		} else {
			//book - число 
			Book bookById = bookRepository.findById(book_id).orElse(null); 
			if (bookById!=null) {
				result = new ArrayList<Book>();
				result.add(bookById);				
			}
		}			
		if ((result == null) || (result.size() == 0)) 
			throw new EntityNotFoundException(String.format("Книга [%s] не найдена", book));
		return result;
	}		
	
	//обработка жанра
	private Genre getOrAddGenre(String genre) throws EntityNotFoundException {
		Genre result = null;
		try {
			result = getGenre(genre);
		} catch (EntityNotFoundException e) {
			if (Helper.getInt(genre)!=-1) throw e;
			else result = addGenre(genre);
		}	
		return result;
	}
	
	//обработка автора
	private Author getOrAddAuthor(String author) throws EntityNotFoundException, NotUniqueEntityFoundException {
		Author result = null;
		try {
			result = getAuthor(author);	
		} catch (EntityNotFoundException e) {
			if (Helper.getInt(author)!=-1) throw e;
			else {
				List<String> names;
				try {
					names = getAuthorNames(author);
				} catch (InvalidValueFormatException e1) {
					throw new EntityNotFoundException(String.format("Автор [%s] не найден: %s", author, e.getMessage()));
				}
				result = addAuthor(names.get(0), names.get(1), (names.size() > 2) ? names.get(2) : null);				
			}
		}	
		return result;
	}
	
	@Transactional 
	public Book addBook(String name, String genre, String author) throws EntityNotFoundException, NotUniqueEntityFoundException {	
		Book result = null;
		
		Genre book_genre = getOrAddGenre(genre);
		Author book_author = getOrAddAuthor(author); 	

		Set<Author> authors = new HashSet<Author>();
		authors.add(book_author);
		
		result = new Book(name, authors, book_genre);
		bookRepository.save(result);
		return result;
	}	
	
	@Transactional 
	public Book editBook(String book, HashMap<String, String> values) throws EntityNotFoundException, InvalidOperationException, NotUniqueEntityFoundException {
		Book result = getBook(book);
		if (values.get("name")!=null) 
			result.setName(values.get("name"));	
		if (values.get("genre")!=null) {
			Genre book_genre = getOrAddGenre(values.get("genre"));
			result.setGenre(book_genre);
		}
		if (values.get("author")!=null) {
			Author book_author = getOrAddAuthor(values.get("author"));
			Set<Author> authors = result.getAuthors();
			int iAuthor = new ArrayList<>(authors).indexOf(book_author);
			if (iAuthor < 0) {
				authors.add(book_author);
				result.setAuthors(authors);				
			}
		}
		
		if (values.get("exAuthor")!=null) {
			Author exAuthor = getAuthor(values.get("exAuthor"));
			Set<Author> authors = result.getAuthors();
			List<Author> authorList = new ArrayList<>(authors);
			int iExAuthor = authorList.indexOf(exAuthor);			
			if (iExAuthor>=0) {
				if (authors.size() == 1) 
					throw new InvalidOperationException("Недопустимая операция: у книги не может быть ни одного автора");
				Author oExAuthor = authorList.get(iExAuthor);
				authors.remove(oExAuthor);	
				result.setAuthors(authors);
			}				
		}

		bookRepository.save(result);		
		
		return result;
	}
	
	@Transactional 
	public void deleteBook(String book) throws EntityNotFoundException, NotUniqueEntityFoundException {
		Book bookToDelete = getBook(book);
		bookRepository.delete(bookToDelete);
	}
	
	@Transactional 
	public void deleteGenre(String genre) throws EntityNotFoundException, InvalidOperationException {
		Genre exGenre = getGenre(genre);
    	HashMap<String, String> filters = new HashMap<>();
    	filters.put("genre_id", String.valueOf(exGenre.getId()));  
		int bookByGenreCount = bookRepository.getBookCount(filters);
		if (bookByGenreCount>0)
			throw new InvalidOperationException("Недопустимая операция: жанр используется");
		genreRepository.delete(exGenre);
	}	
	
	@Transactional 
	public void deleteAuthor(String author) throws EntityNotFoundException, InvalidOperationException, NotUniqueEntityFoundException {
		Author exAuthor = getAuthor(author);
    	HashMap<String, String> filters = new HashMap<>();
    	filters.put("author_id", String.valueOf(exAuthor.getId()));  
		int bookByAuthorCount = bookRepository.getBookCount(filters);
		if (bookByAuthorCount>0) 
			throw new InvalidOperationException("Недопустимая операция: автор используется");
		authorRepository.delete(exAuthor);
	}	
	
	@Transactional 
	public Comment addComment(String book, String score, String content, String commentator) throws EntityNotFoundException, NotUniqueEntityFoundException, InvalidValueFormatException {
		Comment result = null;
		int iScore = Helper.getInt(score);
		if ((iScore < 0) || (iScore >= 6)) 
			throw new InvalidValueFormatException(String.format("Неправильно задана оценка [%s]", score));
		Book commentedBook = getBook(book);
		result = new Comment(commentedBook, (short)iScore, content, commentator);
		commentRepository.save(result);
		return result;
	}
	
	@Transactional 
	public void getCommentAll(HashMap<String, String> filters) {
		HashMap<String, String> newFilters = new HashMap<>();
		if (filters.get("book")!= null && !filters.get("book").isEmpty()) {
			try {
				Book book = getBook(filters.get("book"));
				newFilters.put("book_id", String.valueOf(book.getId()));
			} catch (EntityNotFoundException | NotUniqueEntityFoundException e) {
				newFilters.put("book", filters.get("book"));
			}
		}
		if (filters.get("author")!= null && !filters.get("author").isEmpty()) {
			try {
				Author author = getAuthor(filters.get("author"));
				newFilters.put("author_id", String.valueOf(author.getId()));
			} catch (EntityNotFoundException | NotUniqueEntityFoundException e) {
				newFilters.put("author", filters.get("author"));
			}
		}
		if (filters.get("commentator")!= null && !filters.get("commentator").isEmpty()) {
			newFilters.put("commentator", filters.get("commentator"));
		}   	
		
		fetcher.print(commentRepository::findAllByFilters, 
					  newFilters, 
					  Sort.by("book_id").descending().and(Sort.by("id").ascending()));
		
	}	
	
}
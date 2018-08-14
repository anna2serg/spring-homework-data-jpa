package ru.homework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import ru.homework.domain.Author;
import ru.homework.domain.Book;
import ru.homework.domain.Comment;
import ru.homework.domain.Genre;
import ru.homework.domain.QAuthor;
import ru.homework.domain.QBook;
import ru.homework.domain.QGenre;
import ru.homework.exception.EntityNotFoundException;
import ru.homework.exception.InvalidOperationException;
import ru.homework.exception.InvalidValueFormatException;
import ru.homework.exception.NotUniqueEntityFoundException;
import ru.homework.repository.AuthorRepository;
import ru.homework.repository.BookRepository;
import ru.homework.repository.CommentRepository;
import ru.homework.repository.GenreRepository;

@Service
public class BookcardService {
	
	private final BookRepository bookRepostory;
	private final GenreRepository genreRepostory;
	private final AuthorRepository authorRepostory;
	private final CommentRepository commentRepostory;
	private final FetchDataService fetcher;
	
	public BookcardService(AuthorRepository authorRepostory, BookRepository bookRepostory, GenreRepository genreRepostory, CommentRepository commentRepostory, FetchDataService fetcher) {
		this.authorRepostory = authorRepostory;
		this.bookRepostory = bookRepostory;
		this.genreRepostory = genreRepostory;
		this.commentRepostory = commentRepostory;
		this.fetcher = fetcher;
	}

	private int getInt(String id) {
		int result = -1;
		try {  
			result = Integer.parseInt(id);
	    } catch (NumberFormatException e) {  
	    	// 
	    } 
		return result;
	}

	public Genre getGenre(String genre) throws EntityNotFoundException {
		Genre result = null;
		int genre_id = getInt(genre);		
		if (genre_id == -1) {
			//genre - строка
			result = genreRepostory.findByName(genre).orElse(null);				
		} else {
			//genre - число 
			result = genreRepostory.findById(genre_id).orElse(null); 
		}		
		if (result == null) 
			throw new EntityNotFoundException(String.format("Жанр [%s] не найден", genre));		
		return result;
	}	

	private BooleanBuilder genreBuilder(String name) {
		
		QGenre qGenre = QGenre.genre; 
		BooleanBuilder builder = new BooleanBuilder();
		
		if (name!= null && !name.isEmpty()) {	
			builder.and(qGenre.name.toLowerCase().like("%"+name.toLowerCase()+"%"));
		}	
		
		return builder;
	}	
	
	public void getGenreAll(String name) {
		fetcher.print(genreRepostory::findAll, genreBuilder(name), Sort.by("name").descending());
	}	
	
	@Transactional 
	public Genre addGenre(String name) {
		Genre result = null;
		result = new Genre(name);
		genreRepostory.save(result);
		return result;
	}
	
	@Transactional 
	public Genre editGenre(String genre, String name) throws EntityNotFoundException {
		Genre result = getGenre(genre);
		result.setName(name);
		genreRepostory.save(result);
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
		int author_id = getInt(author);
		if (author_id == -1) {
			//author - строка
			List<String> names;
			try {
				names = getAuthorNames(author);
			} catch (InvalidValueFormatException e) {
				throw new EntityNotFoundException(String.format("Автор [%s] не найден: %s", author, e.getMessage()));
			}
			result = authorRepostory.findBySurnameAndFirstnameAndMiddlename(names.get(0), names.get(1), (names.size() > 2) ? names.get(2) : null);			
		} else {
			//author - число 
			Author authorById = authorRepostory.findById(author_id).orElse(null);
			if (authorById!=null) {
				result = new ArrayList<Author>();
				result.add(authorById);				
			}			
		}
		if ((result == null) || (result.size() == 0)) 
			throw new EntityNotFoundException(String.format("Автор [%s] не найден", author));
		return result;
	}		
		
	private BooleanBuilder authorBuilder(String name) {
		
		QAuthor qAuthor = QAuthor.author;
		BooleanBuilder builder = new BooleanBuilder();
				
		if (name!= null && !name.isEmpty()) {	
			String author = "%"+name.toLowerCase()+"%";		
			builder.and(qAuthor.firstname.toLowerCase().like(author)
				    .or(qAuthor.surname.toLowerCase().like(author))
				    .or(qAuthor.middlename.toLowerCase().like(author)));
		}	
		
		return builder;
	}		
	
	public void getAuthorAll(String name) {
		fetcher.print(authorRepostory::findAll, authorBuilder(name), Sort.by("surname").ascending().and(Sort.by("firstname").ascending()));  		
	}		
	
	@Transactional 
	public Author addAuthor(String surname, String firstname, String middlename) {
		Author result = null;
		result = new Author(surname, firstname, middlename);
		authorRepostory.save(result);
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
		authorRepostory.save(result);
		return result;		
	}		
	
	private BooleanBuilder bookBuilder(HashMap<String, String> filters) {
		
		QBook gBook = QBook.book;
		BooleanBuilder builder = new BooleanBuilder();
		
		if (filters.get("name")!= null && !filters.get("name").isEmpty()) {	
			builder.and(gBook.name.toLowerCase().like("%"+filters.get("name").toLowerCase()+"%"));
		}
		if (filters.get("author")!= null && !filters.get("author").isEmpty()) {
			String author = "%"+filters.get("author").toLowerCase()+"%";		
			builder.and(gBook.authors.any().firstname.toLowerCase().like(author)
				    .or(gBook.authors.any().surname.toLowerCase().like(author))
				    .or(gBook.authors.any().middlename.toLowerCase().like(author)));
		}
		if (filters.get("genre")!= null && !filters.get("genre").isEmpty()) {	
			builder.and(gBook.genre.name.toLowerCase().like("%"+filters.get("genre").toLowerCase()+"%"));
		}	
		if (filters.get("author_id")!= null && !filters.get("author_id").isEmpty()) {
			int author_id = getInt(filters.get("author_id"));
			if (author_id!=-1) 
				builder.and(gBook.authors.any().id.eq(author_id));
		}	
		if (filters.get("genre_id")!= null && !filters.get("genre_id").isEmpty()) {	
			int genre_id = getInt(filters.get("genre_id"));
			if (genre_id!=-1)
				builder.and(gBook.genre.id.eq(genre_id));
		}		
		
		return builder;
	}
	
	public int getBookCount(HashMap<String, String> filters) {
		List<Book> result = new ArrayList<>();
		bookRepostory.findAll(bookBuilder(filters)).forEach(result::add);
		return result.size();
	}
	
	public void getBookAll(HashMap<String, String> filters) {
		fetcher.print(bookRepostory::findAll,bookBuilder(filters), Sort.by("name").ascending());
	}	
	
	public Book getBook(String book) throws EntityNotFoundException, NotUniqueEntityFoundException {		
		List<Book> books =  getBooks(book);
		if (books.size()>1) 
			throw new NotUniqueEntityFoundException(String.format("Найдено более одной книги [%s]", book));	
		return books.get(0);
	}	
	
	public List<Book> getBooks(String book) throws EntityNotFoundException {
		List<Book> result = null;
		int book_id = getInt(book);
		if (book_id == -1) {
			//book - строка
			result = bookRepostory.findByName(book);
		} else {
			//book - число 
			Book bookById = bookRepostory.findById(book_id).orElse(null); 
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
			if (getInt(genre)!=-1) throw e;
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
			if (getInt(author)!=-1) throw e;
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
		bookRepostory.save(result);
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

		bookRepostory.save(result);		
		
		return result;
	}
	
	@Transactional 
	public boolean deleteBook(String book) throws EntityNotFoundException, NotUniqueEntityFoundException {
		boolean result = false;
		Book bookToDelete = getBook(book);
		bookRepostory.delete(bookToDelete);
		result = true;
		return result;
	}
	
	@Transactional 
	public boolean deleteGenre(String genre) throws EntityNotFoundException, InvalidOperationException {
		boolean result = false;
		Genre exGenre = getGenre(genre);
    	HashMap<String, String> filters = new HashMap<>();
    	filters.put("genre_id", String.valueOf(exGenre.getId()));  
		int bookByGenreCount = getBookCount(filters);
		if (bookByGenreCount>0)
			throw new InvalidOperationException("Недопустимая операция: жанр используется");
		genreRepostory.delete(exGenre);
		result = true;
		return result;
	}	
	
	@Transactional 
	public boolean deleteAuthor(String author) throws EntityNotFoundException, InvalidOperationException, NotUniqueEntityFoundException {
		boolean result = false;
		Author exAuthor = getAuthor(author);
    	HashMap<String, String> filters = new HashMap<>();
    	filters.put("author_id", String.valueOf(exAuthor.getId()));  
		int bookByAuthorCount = getBookCount(filters);
		if (bookByAuthorCount>0) 
			throw new InvalidOperationException("Недопустимая операция: автор используется");
		authorRepostory.delete(exAuthor);
		result = true;
		return result;
	}	
	
	@Transactional 
	public Comment addComment(String book, String score, String content, String commentator) throws EntityNotFoundException, NotUniqueEntityFoundException, InvalidValueFormatException {
		Comment result = null;
		int iScore = getInt(score);
		if ((iScore < 0) || (iScore >= 6)) 
			throw new InvalidValueFormatException(String.format("Неправильно задана оценка [%s]", score));
		Book commentedBook = getBook(book);
		result = new Comment(commentedBook, (short)iScore, content, commentator);
		commentRepostory.insert(result);
		return result;
	}
	
	@Transactional 
	public List<Comment> getComments(String book) throws EntityNotFoundException, NotUniqueEntityFoundException {
		Book commentedBook = getBook(book);
		return new ArrayList<>(commentedBook.getComments());
	}
	
	@Transactional 
	public List<Comment> getCommentAll(HashMap<String, String> filters) {
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
		return commentRepostory.getAll(newFilters);
	}	
	
}
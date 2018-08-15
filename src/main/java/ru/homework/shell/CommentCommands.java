package ru.homework.shell;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ru.homework.exception.EntityNotFoundException;
import ru.homework.exception.InvalidValueFormatException;
import ru.homework.exception.NotUniqueEntityFoundException;
import ru.homework.service.BookcardService;

@ShellComponent
public class CommentCommands {
	
	private final BookcardService service;
	
	@Autowired
    public CommentCommands(BookcardService service) {
		this.service = service;		
	}	  	
	
    @ShellMethod(value = "показать комментарии")
    public void getCommentAll(
    		@ShellOption(help="ИД или наименование книги", defaultValue=ShellOption.NULL) String book,    		
    		@ShellOption(help="ИД или имя автора", defaultValue=ShellOption.NULL) String author,
    		@ShellOption(help="имя комментатора", defaultValue=ShellOption.NULL) String commentator) {
    	HashMap<String, String> filters = new HashMap<>();
    	if (book != null && !book.isEmpty()) 
    		filters.put("book", book);
    	if (author != null && !author.isEmpty()) 
    		filters.put("author", author);    	
    	if (commentator != null && !commentator.isEmpty()) 
    		filters.put("commentator", commentator);   	
    	service.getCommentAll(filters);
    }
	
    @ShellMethod(value = "добавить комментарий к книге")
    public void addComment(
    		@ShellOption(help="ИД или наименование книги") String book,
    		@ShellOption(help="оценка читателя: 0-5") String score,
    		@ShellOption(help="комментарий", defaultValue=ShellOption.NULL) String content,
    		@ShellOption(help="комментатор", defaultValue=ShellOption.NULL) String commentator) {   	
        try {
        	System.out.println(service.addComment(book, score, content, commentator));		  	
		} catch (EntityNotFoundException | NotUniqueEntityFoundException | InvalidValueFormatException e) {
			System.out.println(e.getMessage());
		}
    }   	
	
}

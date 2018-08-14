package ru.homework.service;

import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import ru.homework.configuration.AppSettings;

@Service
public class FetchDataService {

	private final AppSettings settings;
	private final Scanner in;
	
	@Autowired
	public FetchDataService(AppSettings settings) {
		this.settings = settings;
		this.in = new Scanner(System.in);
	}
	
	public <T> void output(List<T> data) {
    	int i = 0;
    	String userInput = "";
		for (T item : data) {
			System.out.println(item);
			i++;
			if ((i % settings.getFetchsize() == 0) && (i < data.size())) {
				System.out.println("Для продолжения вывода - нажмите Enter, для прерывания - введите любой символ, затем Enter");
				userInput = in.nextLine();
				if (!userInput.equals("")) break;
			}			 
		}		
	}
	
	public <T> void print(BiFunction<BooleanBuilder, Pageable, Page<T>> funcRef, BooleanBuilder builder,  Sort sort) {
    	String userInput = "";
    	Pageable pageable = PageRequest.of(0, settings.getFetchsize(), sort);
		while(true){
			Page<T> page = funcRef.apply(builder, pageable);    
            System.out.println("Страница " + (page.getNumber() + 1) + " из " + page.getTotalPages()); 
            page.getContent().forEach(System.out::println);

            if(!page.hasNext()){
                break;
            } else {
    			System.out.println("Для продолжения вывода - нажмите Enter, для прерывания - введите любой символ, затем Enter");
    			userInput = in.nextLine();
    			if (!userInput.equals("")) break;                 	
            }
            pageable = page.nextPageable();
        }
	}
	
}

package com.javawiz;

import com.javawiz.entity.Book;
import com.javawiz.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	// init bean to insert 3 books into h2 database.
	@Bean
	CommandLineRunner initDatabase(BookRepository repository) {
		return args -> {
			List<Book> books = new ArrayList<Book>();
			books.add(Book.builder()
					.name("The Intelligent Investor")
					.author("Benjamin Graham")
					.price(new BigDecimal("232.75"))
					.build());
			books.add(Book.builder()
					.name("Rich Dad Poor Dad")
					.author("Robert T. Kiyosaki")
					.price(new BigDecimal("157.75"))
					.build());
			books.add(Book.builder()
					.name("Meditation And Its Methods")
					.author("Swami Vivekananda")
					.price(new BigDecimal("17.75"))
					.build());
			books.add(Book.builder()
					.name("The Richest Man In Babylon")
					.author("George S Clason")
					.price(new BigDecimal("150.40"))
					.build());
			repository.saveAll(books);
		};
	}

}

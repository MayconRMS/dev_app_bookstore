package br.com.mrms.bookstore.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.mrms.bookstore.exception.BookExistException;
import br.com.mrms.bookstore.exception.BookNotFoundException;
import br.com.mrms.bookstore.model.Book;

public class BookRepository {

	private Map<Long, Book> books = new HashMap<>();

	// Apenas para validação do insert
	private static BookRepository repo;

	// Apenas para validação do insert
	public static BookRepository getInstance() {
		if (repo == null) {
			repo = new BookRepository();
		}
		return repo;
	}

	private BookRepository() {
		Book book1 = new Book(1L, "Livro A", "ISBN-1234", "GENERO A", 23.99, "Author 1");
		Book book2 = new Book(2L, "Livro B", "ISBN-1235", "GENERO B", 23.99, "Author 2");

		books.put(book1.getId(), book1);
		books.put(book2.getId(), book2);

	}

	public List<Book> getBooks() {
		return new ArrayList<>(books.values());
	}

	public Book getBookByIsbn(String isbn) {
		for (Book book : books.values()) {
			if (isbn.equals(book.getCodeIsbn())) {
				return book;
			}
		}
		throw new BookNotFoundException();
	}

	public void insertBook(Book book) {
		if (books.containsKey(book.getId())) {
			throw new BookExistException();
		}
		books.put(book.getId(), book);
	}

	public void updateBook(Book book) {
		books.put(book.getId(), book);
	}

	public void removeBook(Long id) {
		if (books.containsKey(id)) {
			books.remove(id);
		} else {
			throw new BookNotFoundException();
		}
	}

}

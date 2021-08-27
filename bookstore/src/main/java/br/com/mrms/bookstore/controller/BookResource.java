package br.com.mrms.bookstore.controller;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.mrms.bookstore.exception.BookExistException;
import br.com.mrms.bookstore.exception.BookNotFoundException;
import br.com.mrms.bookstore.model.Book;
import br.com.mrms.bookstore.model.Books;
import br.com.mrms.bookstore.model.ItemSearch;
import br.com.mrms.bookstore.repository.BookRepository;

import javax.ws.rs.core.UriBuilder;

@Path("book")
public class BookResource {

	BookRepository bookRepository = BookRepository.getInstance();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Books getBooks() {
		Books books = new Books();
		books.setBooks(bookRepository.getBooks());
		return books;
	}

	@GET
	@Path("/{isbn}")
	public ItemSearch getBookByIsbn(@PathParam("isbn") String isbn) {
		try {
			Book book = bookRepository.getBookByIsbn(isbn);
			
			ItemSearch itemSearch = new ItemSearch();
			itemSearch.setBook(book);
			
			Link link = Link.fromUri("/shopping/" + book.getId())
					.rel("shopping")
					.type("POST").build();
			
			itemSearch.addLink(link);
			
			return itemSearch;
		} catch (BookNotFoundException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertBook(Book book) {
		try {
			bookRepository.insertBook(book);
		} catch (BookExistException e) {
			throw new WebApplicationException(Status.CONFLICT);
		}

		URI uriLocation = UriBuilder.fromPath("book/{isbn}").build(book.getCodeIsbn());

		return Response.created(uriLocation).entity(book).build();
	}

	@PUT
	@Path("/{isbn}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertBook(@PathParam("isbn") String isbn, Book book) {
		try {
			Book bookBase = bookRepository.getBookByIsbn(isbn);
			
			bookBase.setAuthor(book.getAuthor());
			bookBase.setGenere(book.getGenere());
			bookBase.setPrice(book.getPrice());
			bookBase.setTitle(book.getTitle());
			
			bookRepository.updateBook(book);
		} catch (BookNotFoundException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return Response.ok().entity(book).build();
	}
	
	@DELETE
	@Path("/{id}")
	public void removeBook(@PathParam("id") Long id) {
		try {
			bookRepository.removeBook(id);
		} catch (BookNotFoundException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

	}

}

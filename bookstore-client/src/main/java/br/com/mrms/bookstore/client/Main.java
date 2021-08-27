package br.com.mrms.bookstore.client;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import br.com.mrms.bookstore.client.model.Book;
import br.com.mrms.bookstore.client.model.Books;
import br.com.mrms.bookstore.client.model.ItemSearch;


public class Main {

	private static String urlBookStore = "https://localhost:8443/bookstore";
	private static Client client = null;

	public static void main(String[] args) {

		authentication();
		
		System.out.println("All");
		getAll();

		System.out.println("Insert ISBN-9999");
		post();

		System.out.println("All ISBN-9999");
		delete();

		System.out.println("All");
		getAll();

	}

	private static void authentication() {
		//Passando certificado digital auto assinado
		SslConfigurator config = SslConfigurator.newInstance()
				.trustStoreFile("server.keystore")
				.trustStorePassword("bookstore");
		
		SSLContext context = config.createSSLContext();
		
		client = ClientBuilder.newBuilder().sslContext(context).build();
		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic("admin", "password");
		client.register(auth);

	}

	private static void delete() {
		Response response = client.target(urlBookStore).path("book").path("3")
				.request(MediaType.APPLICATION_JSON).delete();

		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));

		if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			System.out.println("Book removed ");
		} else {
			System.err.println("Error not found: Code Status " + response.getStatus());
		}

	}

	private static void post() {
		Book bookNew = new Book(3L, "Livro C", "ISBN-9999", "GENERO C", 125.99, "Author 3");

		Response response = client.target(urlBookStore).path("book").request()
				.post(Entity.json(bookNew));

		if (response.getStatus() == Status.CREATED.getStatusCode()) {
			ItemSearch item = new ItemSearch();

			item = client.target(response.getLocation()).request(MediaType.APPLICATION_XML)
					.get(ItemSearch.class);

			System.out.println("Book: " + item.getBook());

		} else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
			System.err.println("Book not create ");
		} else {
			System.err.println("Error not found: Code Status " + response.getStatus());
		}

	}

	public static void getAll() {
		/*
		 * Client client = ClientBuilder.newClient();
		 * 
		 * WebTarget targetBase = client.target("http://localhost:8080/bookstore");
		 * 
		 * WebTarget targetBook = targetBase.path("book");
		 * 
		 * Builder request = targetBook.request(MediaType.APPLICATION_JSON); Books books
		 * = request.get(Books.class);
		 * 
		 * for (Book book : books.getBooks()) { System.out.println("Livro: " +
		 * book.getTitle()); }
		 */

		// Simplificada

		Books booksSimple = client.target(urlBookStore).path("book")
				.request(MediaType.APPLICATION_JSON).get(Books.class);

		for (Book book : booksSimple.getBooks()) {
			System.out.println("Livro: " + book);
		}

	}

}

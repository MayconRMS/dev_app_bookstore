package br.com.mrms.bookstore.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Book {

	@XmlAttribute
	private Long id;
	@XmlElement
	private String title;
	@XmlElement
	private String codeIsbn;
	@XmlElement
	private String genere;
	@XmlElement
	private Double price;
	@XmlElement
	private List<Author> author = new ArrayList<Author>();

	public Book(Long id, String title, String codeIsbn, String genere, Double price, String... nomes) {
		this.id = id;
		this.title = title;
		this.codeIsbn = codeIsbn;
		this.genere = genere;
		this.price = price;

		for (String nome : nomes) {
			author.add(new Author(nome));
		}
	}

}

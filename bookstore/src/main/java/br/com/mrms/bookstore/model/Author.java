package br.com.mrms.bookstore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "author")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Author {

	@XmlAttribute
	private Long id;
	@XmlElement
	private String nome;

	public Author(String nome) {
		this.id = Math.round(Math.random() * 10);
		this.nome = nome;
	}
}

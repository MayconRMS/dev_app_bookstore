package br.com.mrms.bookstore.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "books")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Books {

	@XmlElement(name = "book")
	private List<Book> books = new ArrayList<Book>();

}

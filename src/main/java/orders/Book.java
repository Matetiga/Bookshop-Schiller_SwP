package orders;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import jakarta.persistence.Entity;

@Entity
public class Book extends Product {
	private String author;
	private Genre genre;

	public static enum Genre {
		Thriller, Fantasy;
	}

	public Book(String name, Money price, Genre genre, String author) {

		super(name, price);

		this.genre = genre;
		this.author = author;
	}

	public String getName(){
		return super.getName();
	}
}

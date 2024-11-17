package kickstart.orders;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import jakarta.persistence.Entity;

//class is only for demonstration and testing, can be deleted after demonstration of the prototype
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

	public String getAuthor(){
		return this.author;
	}
}

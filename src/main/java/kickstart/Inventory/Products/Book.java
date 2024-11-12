package kickstart.Inventory.Products;
import jakarta.persistence.Embedded;
import org.javamoney.moneta.Money;

import java.util.Set;

// TODO check for @Embeddable and @Embedded
public class Book extends ShopProduct {
	@Embedded
	private Genre genre;
	private String author;
	private String ISBN;
	private String publisher;

	public Book(String name, String image, Money price, int id, String description, Genre genre, String author, String ISBN, String publisher) {
		super(name, image, price, id, description);
		if (genre == null) {
			throw new IllegalArgumentException("Book Genre cannot be null");
		}

		if(author == null){
			throw new NullPointerException("Book Author cannot be null");
		}
		if(author.isBlank()){
			throw new IllegalArgumentException("Book Author cannot be empty");
		}

		if(ISBN == null){
			throw new NullPointerException("Book ISBN cannot be null");
		}
		if(ISBN.isBlank()){
			throw new IllegalArgumentException("Book ISBN cannot be empty");
		}

		if(publisher == null){
			throw new NullPointerException("Book Publisher cannot be null");
		}
		if(publisher.isBlank()){
			throw new IllegalArgumentException("Book Publisher cannot be empty");
		}

		this.genre = genre;
		this.author = author;
		this.ISBN = ISBN;
		this.publisher = publisher;
	}



	public void setProductGenre(Genre genre) {
		// the idea is that Genres are a list
		// so the admin has to chose from the list the genres to bind it to a product
		// before adding a product with a new genre
		// the genre should be added -> is this what we want
		// or should the admin be able to add a new genre on the fly

		// test for illegal values ?

		Set<String> allGenres = genre.getGenres();
		if (!allGenres.contains(genre.toString())) {
			throw new IllegalArgumentException("Genre does not exist");
			// return or Exception?
		}
		this.genre = genre;
	}

	public Genre getProductGenre() {
		// should this be made for each product
		// what about the Merch and calendar, which do not have a genre
		return genre;
	}
}




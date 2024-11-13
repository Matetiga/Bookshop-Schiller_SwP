package kickstart.Inventory.Products;
import jakarta.persistence.Embedded;
import org.javamoney.moneta.Money;

import java.util.Set;

import static kickstart.Inventory.Products.Genre.getAllGenres;

// TODO check for @Embeddable and @Embedded
// TODO Add a Valid ISBN check
// IMPORTANT: ISBN management should be the same as the Genre Management
// that would mean another class, which should have a static Set of all ISBNs, ASK!!!!!
// or is there a better way to manage every ID, ISBN, Genre, Book collection...
public class Book extends ShopProduct {
	@Embedded
	private Genre genre;
	private String author;
	private String ISBN;
	private String publisher;

	public Book(String name, String image, Money price, String description,
				Genre genre, String author, String ISBN, String publisher) {
		super(name, image, price, description);
		if (genre == null) {
			throw new NullPointerException("Book Genre cannot be null");
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

	// Setters
	public void setBookGenre(Genre genre) {
		if (genre == null) {
			throw new NullPointerException("Setter Book Genre cannot be null");
		}
		Set<Genre> allGenres = getAllGenres();
		if (!allGenres.contains(genre)) {
			throw new IllegalArgumentException("Setter Book Genre does not exist");
			// return or Exception?
		}
		this.genre = genre;
	}

	public void setAuthor(String author) {
		if (author == null) {
			throw new NullPointerException("Setter Book Author cannot be null");
		}
		if(author.isBlank()){
			throw new IllegalArgumentException("Setter Book Author cannot be empty");
		}
		this.author = author;
	}

	public void setGenre(Genre genre){
		if (genre == null) {
			throw new NullPointerException("Setter Book Genre cannot be null");
		}
		// TODO should it throw an Exception or create automatically the genre?
		if(!getAllGenres().contains(genre)){
			throw new IllegalArgumentException("Setter Book Genre does not exist");
		}

		this.genre = genre;
	}

	// Getters
	public Genre getGenre() {
		return genre;
	}
	public String getAuthor() {
		return author;
	}
	public String getISBN() {
		return ISBN;
	}
	public String getPublisher() {
		return publisher;
	}

}




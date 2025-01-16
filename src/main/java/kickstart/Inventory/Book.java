package kickstart.Inventory;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;

import java.util.HashSet;
import java.util.Set;

import static kickstart.Inventory.Genre.getAllGenres;

@Entity
public class Book extends ShopProduct {
	@ElementCollection
	private Set<Genre> genres = new HashSet<>();
	private String author;
	private String ISBN;
	private String publisher;

	/**
	 * empty constructor
	 */
	public Book(){}

	/**
	 * Basic constructor
	 * @param name
	 * @param image
	 * @param price
	 * @param description
	 * @param genres
	 * @param author
	 * @param ISBN
	 * @param publisher
	 */
	public Book(String name, String image, Money price, String description,
				Set<Genre> genres, String author, String ISBN, String publisher) {
		super(name, image, price, description);
		if (genres.isEmpty()) {
			throw new IllegalArgumentException("Book Genre cannot be empty");
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

		this.genres = genres;
		this.author = author;
		this.ISBN = ISBN;
		this.publisher = publisher;
	}

	// Setters

	/**
	 *
	 * @param genre
	 */
	public void addBookGenre(Genre genre) {
		if (genre == null) {
			throw new NullPointerException("Setter Book Genre cannot be null");
		}
		if (!getAllGenres().contains(genre)) {
			throw new IllegalArgumentException("Setter addBookGenre does not exist in getAllGenres");
		}
		if(genres.contains(genre)){
			throw new IllegalArgumentException("Setter addBookGenre already exists");
		}
		this.genres.add(genre);
	}

	/**
	 *
	 * @param genre
	 */
	public void deleteBookGenre(Genre genre){
		if (genre == null) {
			throw new NullPointerException("Setter deleteBookGenre cannot be null");
		}
		if (!getAllGenres().contains(genre)) {
			throw new IllegalArgumentException("Setter deleteBookGenre does not exist in getAllGenres");
		}
		if(!genres.contains(genre)){
			throw new IllegalArgumentException("Setter deleteBookGenre does not exist");
		}
		this.genres.remove(genre);
	}

	/**
	 *
	 * @param genres
	 */
	public void setBookGenres(Set<Genre> genres) {
		if (genres.isEmpty()) {
			throw new IllegalArgumentException("Setter Book Genre cannot be empty");
		}
		this.genres = genres;
	}

	/**
	 *
	 * @param author
	 */
	public void setAuthor(String author) {
		if (author == null) {
			throw new NullPointerException("Setter Book Author cannot be null");
		}
		if(author.isBlank()){
			throw new IllegalArgumentException("Setter Book Author cannot be empty");
		}
		this.author = author;
	}


	public void setPublisher(String publisher) {
		if (publisher == null) {
			throw new NullPointerException("Setter Book Publisher cannot be null");
		}
		if(publisher.isBlank()){
			throw new IllegalArgumentException("Setter Book Publisher cannot be empty");
		}
		this.publisher = publisher;
	}

	//TODO check for a valid ISBN

	/**
	 *
	 * @param ISBN
	 */
	public void setISBN(String ISBN) {
		if (ISBN == null) {
			throw new NullPointerException("Setter Book ISBN cannot be null");
		}
		if(ISBN.isBlank()){
			throw new IllegalArgumentException("Setter Book ISBN cannot be empty");
		}
		this.ISBN = ISBN;
	}

	// Getters

	/**
	 *
	 * @return
	 */
	public Set<Genre> getBookGenres() {
		return genres;
	}

	/**
	 *
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 *
	 * @return
	 */
	public String getISBN() {
		return ISBN;
	}

	/**
	 *
	 * @return
	 */
	public String getPublisher() {
		return publisher;
	}

}




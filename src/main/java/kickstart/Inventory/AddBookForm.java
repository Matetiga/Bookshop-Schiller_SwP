package kickstart.Inventory;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

public class AddBookForm {

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Image is required")
	private String image;

	@Min(value = 0, message = "Price can not be negative")
	private double price;

	@NotBlank(message = "Description is required")
	private String description;

	@NotEmpty(message = "Genre is required")
	private Set<String> genre = new HashSet<>();

	@NotBlank(message = "Author is required")
	private String author;

	@NotBlank(message = "ISBN is required")
	private String ISBN;

	@NotBlank(message = "Publisher is required")
	private String publisher;

	@Min(value = 0, message = "Stock can not be negative")
	private int stock;

	/**
	 *
	 *Basic empty constructor
	 */
	public AddBookForm() {}

	/**
	 * Constructor
	 * @param name
	 * @param image
	 * @param description
	 * @param genre
	 * @param author
	 * @param ISBN
	 * @param publisher
	 * @param price
	 * @param stock
	 */
	public AddBookForm(String name, String image, String description, Set<String> genre,
					   String author, String ISBN, String publisher, double price, int stock) {
		this.name = name;
		this.image = image;
		this.price = price;
		this.description = description;
		this.genre = genre;
		this.author = author;
		this.ISBN = ISBN;
		this.publisher = publisher;
		this.stock = stock;
	}

	// Getters and setters

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public String getImage() {
		return image;
	}

	/**
	 *
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 *
	 * @return
	 */
	public double getPrice() {
		return price;
	}

	/**
	 *
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getGenre() {
		return genre;
	}

	/**
	 *
	 * @param genre
	 */
	public void setGenre(Set<String> genre) {
		this.genre = genre;
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
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
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
	 * @param ISBN
	 */
	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	/**
	 *
	 * @return
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 *
	 * @param publisher
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 *
	 * @return
	 */
	public int getStock() {
		return stock;
	}

	/**
	 *
	 * @param stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}
}

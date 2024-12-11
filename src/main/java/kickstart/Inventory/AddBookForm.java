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
	private MultipartFile image;

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

	public AddBookForm() {}
	public AddBookForm(String name, MultipartFile image, String description, Set<String> genre,
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getGenre() {
		return genre;
	}

	public void setGenre(Set<String> genre) {
		this.genre = genre;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}

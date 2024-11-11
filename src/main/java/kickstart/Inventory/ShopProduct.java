package kickstart.Inventory;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;

import java.util.ArrayList;

@Entity
public class ShopProduct extends Product {

	public static enum ProductType {
		CALENDER, BOOK, MERCH;
	}


	private String image;
	private ProductType type;
	private Genre genre;
	private int id;

	public ShopProduct() {}
	public ShopProduct(String name, String image, Money price, Genre genre, ProductType type, int id) {
		// should we test for illegal values?

		super(name, price);
		this.image = image;
		this.genre = genre;
		this.type = type;

		// InventoryItemIdentifier -> from SalesPoint
		// use that or something else
		this.id = id;
	}

	// Setters
	public void setProductGenre(Genre genre) {
		// the idea is that Genres are a list
		// so the admin has to chose from the list the genres to bind it to a product
		// before adding a product with a new genre
		// the genre should be added -> is this what we want
		// or should the admin be able to add a new genre on the fly

		// test for illegal values ?

		ArrayList<String> allGenres = genre.getGenres();
		if (!allGenres.contains(genre)) {
			throw new IllegalArgumentException("Genre does not exist");
			// return or Exception?
		}
		this.genre = genre;
	}

	// Getters
	// why is this throwing an error
	public Genre getProductGenre() {
		// should this be made for each product
		// what about the Merch and calendar, which do not have a genre
		return genre;
	}
	public String getImage() {
		return image;
	}
	public ProductType getType() {
		return type;
	}
	public int getProductId() {
		return id;
	}

}
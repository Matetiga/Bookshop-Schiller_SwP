package kickstart.Inventory;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;

import java.util.List;

@Entity
public class ShopProduct extends Product {

	public static enum ProductType {
		CALENDER, BOOK, MERCH;
	}


	@Embedded
	private Genre genre;

	private String image;
	private ProductType type;
	private int id;

	// shows that this function is deprecated
	// public ShopProduct() {} //Tu funcion me arrojaba un error, todo lo que me provocaba error lo comente
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

		List<String> allGenres = genre.getGenres();
		if (!allGenres.contains(genre.toString())) {
			throw new IllegalArgumentException("Genre does not exist");
			// return or Exception?
		}
		this.genre = genre;
	}

	// Getters
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
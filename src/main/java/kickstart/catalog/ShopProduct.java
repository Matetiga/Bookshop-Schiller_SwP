package kickstart.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

// TODO: Umbenennen da wir es eigentlich Product nennen wollten, nur ist im VideoShop die Class Disc auf extends Product gesetzt
public class ShopProduct extends Product {

	public static enum ProductType {
		CALENDER, BOOK, MERCH;
	}

	private String genre, image;
	private ProductType type;

	public ShopProduct() {}

	public ShopProduct(String name, String image, Money price, String genre, ProductType type) {
		super(name, price);

		this.image = image;
		this.genre = genre;
		this.type = type;
	}

	public String getGenre() { return genre; }

	public String getImage() {
		return image;
	}

	public ProductType getType() {
		return type;
	}

}

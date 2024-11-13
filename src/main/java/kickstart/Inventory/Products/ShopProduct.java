package kickstart.Inventory.Products;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.salespointframework.catalog.Product;

import java.util.List;

@Entity
public class ShopProduct extends Product {

	private String name;
	private Money price;
	private String image;
	private int id;
	private String description;

	@SuppressWarnings({"deprecation"})
	protected ShopProduct() {}
	public ShopProduct(String name, String image, Money price, int id, String description) {
		// TODO is this the most optimal way or is it better to replace it for a factory method?
		// TODO should it be possible to create a Product or ONLY a BOOK, CALENDAR OR MERCH?

		super(name, price);
		if (image == null) {
			throw new NullPointerException("ShopProduct Image cannot be null");
		}

		if(image.isBlank()){
			throw new IllegalArgumentException("ShopProduct image cannot be blank");
		}

		if(description == null){
			throw new NullPointerException("ShopProduct Description cannot be null");
		}
		if(description.isBlank()){
			throw new IllegalArgumentException("ShopProduct Description cannot be blank");
		}

		this.name = name;
		this.price = price;
		this.image = image;
		this.id = id;
		this.description = description;

	}

	// Setters
	// setters for name and price are already given by the Product class (setName() and setPrice())

	public void setDescription(String description) {
		if (description == null){
			throw new NullPointerException("Product Description cannot be null");
		}
		if(description.isBlank()){
			throw new IllegalArgumentException("Product Description cannot be blank");
		}
		this.description = description;
	}

	public void setImage(String image) {
		if (image == null){
			throw new NullPointerException("ShopProduct Image cannot be null");
		}
		if(image.isBlank()){
			throw new IllegalArgumentException("ShopProduct Image cannot be blank");
		}
		this.image = image;
	}

	// TODO
	// how should the ID system be handled
	// maybe should be done with InventoryItemIdentifier
	// but then how managed (how to change it? how to get it? is it part of the Product?)
	public void setProductId(int id) {
		if (id < 0){
			throw new IllegalArgumentException("Product ID cannot be negative");
		}
		this.id = id;
	}




	// Getters
	// getters for name and price are already given by the Product class (getName() and getPrice())

	public String getImage() {
		return image;
	}

	public int getProductId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
package kickstart.Inventory.Products;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import java.util.List;

@Entity
public abstract class ShopProduct extends Product {


	private String image;
	private int id;
	private String description;

	@SuppressWarnings({"deprecation"})
	protected ShopProduct() {}
	public ShopProduct(String name, String image, Money price, int id, String description) {
		// is this the most optimal way or is it better to replace it for a factory method?

		// TODO : add checks for illegal values
		super(name, price);
		this.image = image;
		this.id = id;
		this.description = description;

	}

	// Setters
	public void setDescription(String description) {
		if (description == null){
			throw new NullPointerException("Product Description cannot be null");
		}
		if(description.isBlank()){
			throw new IllegalArgumentException("Product Description cannot be empty");
		}
		this.description = description;
	}

	public void setImage(String image) {
		if (image == null){
			throw new NullPointerException("Product Image cannot be null");
		}
		if(image.isBlank()){
			throw new IllegalArgumentException("Product Image cannot be empty");
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

	// TODO
	// name setter ?
	// money setter ?



	// Getters

	public String getImage() {
		return image;
	}

	public int getProductId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	// TODO
	// name, price getters?

}
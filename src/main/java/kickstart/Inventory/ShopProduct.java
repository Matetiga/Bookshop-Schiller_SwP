package kickstart.Inventory;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public abstract class ShopProduct extends Product{

	private String name;
	private Money price;
	private String image;
	private String description;


	@SuppressWarnings({"deprecation"})
	/**
	 * Empty constructor
	 */
	protected ShopProduct() {}

	/**
	 * Basic constructor
	 * @param name
	 * @param image
	 * @param price
	 * @param description
	 */
	public ShopProduct(String name, String image, Money price, String description) {

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
		this.description = description;


	}

	// Setters
	// setters for name and price are already given by the Product class (setName() and setPrice())

	/**
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		if (description == null){
			throw new NullPointerException("ShopProduct Description cannot be null");
		}
		if(description.isBlank()){
			throw new IllegalArgumentException("ShopProduct Description cannot be blank");
		}
		this.description = description;
	}

	/**
	 *
	 * @param image
	 */
	public void setImage(String image) {
		if (image == null){
			throw new NullPointerException("ShopProduct Image cannot be null");
		}
		if(image.isBlank()){
			throw new IllegalArgumentException("ShopProduct Image cannot be blank");
		}
		this.image = image;
	}


	// Getters
	// getters for name and price are already given by the Product class (getName() and getPrice())

	/**
	 *
	 * @return
	 */
	public String getImage() {
		return image;
	}

	/**
	 *
	 * @return
	 */
	public ProductIdentifier getProductId() {
		return this.getId();
	}

	/**
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}


}
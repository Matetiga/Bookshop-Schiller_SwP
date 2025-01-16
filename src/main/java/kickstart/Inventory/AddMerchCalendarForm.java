package kickstart.Inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddMerchCalendarForm {
	@NotBlank(message = "Name is required")
	private String name;

	@Min(value = 0, message = "Stock can not be negative")
	private int stock;

	@NotNull(message = "Image is required")
	private String image;

	@Min(value = 0, message = "Price can not be negative")
	private double price;

	@NotBlank(message = "Description is required")
	private String description;

	/**
	 * Empty constructor
	 */
	public AddMerchCalendarForm() {}

	/**
	 * Constructor
	 * @param name
	 * @param image
	 * @param price
	 * @param description
	 * @param stock
	 */
	public AddMerchCalendarForm(String name, String image, double price, String description, int stock) {
		this.name = name;
		this.image = image;
		this.price = price;
		this.description = description;
		this.stock = stock;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
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
	 * @return
	 */
	public double getPrice() {
		return price;
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
	 * @return
	 */
	public int getStock() {
		return stock;
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
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
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
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @param stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

}

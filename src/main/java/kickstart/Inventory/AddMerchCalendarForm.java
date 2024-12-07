package kickstart.Inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddMerchCalendarForm {
	@NotBlank(message = "Name is required")
	private String name;

	@Min(value = 1, message = "Stock must be greater than 0")
	private int stock;

	@NotNull(message = "Image is required")
	private MultipartFile image;

	@Min(value = 0, message = "Price can not be negative")
	private double price;

	@NotBlank(message = "Description is required")
	private String description;


	public AddMerchCalendarForm() {
	}
	public AddMerchCalendarForm(String name, MultipartFile image, double price, String description, int stock) {
		this.name = name;
		this.image = image;
		this.price = price;
		this.description = description;
		this.stock = stock;
	}

	public String getName() {
		return name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public int getStock() {
		return stock;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}

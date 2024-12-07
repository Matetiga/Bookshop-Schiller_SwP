package kickstart.Inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class TestingForm {
	@NotBlank(message = "Name is required")
	private String name;

//	@Min(value = 0, message = "Price can not be negative")
//	private double price;

	public TestingForm(String name) {
		this.name = name;
//		this.price = price;
	}

	public String getName() {
		return name;
	}

//	public double getPrice() {
//		return price;
//	}
}

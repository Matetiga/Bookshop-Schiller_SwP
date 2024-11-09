package kickstart.Inventory;

public class Product {

    private String name;
    private int price;
    private String description;

    public Product(String name, int price, String description) {
		if (name == null || description == null){
			throw new NullPointerException("name and description can not be null");
		}
        if (name.isBlank() || description.isBlank()) {
			throw new IllegalArgumentException("Name and description must not be empty");
        }
		if (price < 0) {
			throw new IllegalArgumentException("Price must not be negative");
		}
		this.name = name;
		this.price = price;
		this.description = description;
    }

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}
}


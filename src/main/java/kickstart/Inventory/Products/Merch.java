package kickstart.Inventory.Products;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;

@Entity
public class Merch extends ShopProduct {

	public Merch(String name, String image, Money price, String description) {
		super(name, image, price, description);
	}

	public Merch() {

	}
}

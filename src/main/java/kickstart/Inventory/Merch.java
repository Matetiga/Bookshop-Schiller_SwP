package kickstart.Inventory;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;

@Entity
public class Merch extends ShopProduct {
	/**
	 * Basic constructor
	 * @param name
	 * @param image
	 * @param price
	 * @param description
	 */
	public Merch(String name, String image, Money price, String description) {
		super(name, image, price, description);
	}

	/**
	 * Empty constructor
	 */
	public Merch() {

	}
}

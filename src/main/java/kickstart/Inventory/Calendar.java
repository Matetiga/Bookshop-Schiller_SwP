package kickstart.Inventory;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;

@Entity
public class Calendar extends ShopProduct {
	/**
	 * Basic Constructor
	 * @param name
	 * @param image
	 * @param price
	 * @param description
	 */
	public Calendar(String name, String image, Money price,  String description) {
		super(name, image, price, description);
	}

	/**
	 * Empty constructor
	 */
	public Calendar() {

	}
}

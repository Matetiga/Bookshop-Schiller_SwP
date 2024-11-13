package kickstart.Inventory.Products;

import org.javamoney.moneta.Money;

public class Calendar extends ShopProduct {
	public Calendar(String name, String image, Money price, int id, String description) {
		super(name, image, price, id, description);
	}
}

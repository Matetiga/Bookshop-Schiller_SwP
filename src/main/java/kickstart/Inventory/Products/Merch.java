package kickstart.Inventory.Products;

import org.javamoney.moneta.Money;

public class Merch extends ShopProduct {

	public Merch(String name, String image, Money price, String description) {
		super(name, image, price, description);
	}
}

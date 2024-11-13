package kickstart.Inventory.Products;

import org.javamoney.moneta.Money;

public class Merch extends ShopProduct {

	public Merch(String name, String image, Money price, int id, String description) {
		super(name, image, price, id, description);
	}
}

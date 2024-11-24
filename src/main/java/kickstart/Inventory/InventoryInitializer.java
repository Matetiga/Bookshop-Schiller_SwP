package  kickstart.Inventory;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import static kickstart.Inventory.Genre.createGenre;

@Component
public class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> shopProductInventory;
	private final ShopProductCatalog shopProductCatalog;

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> shopProductInventory, ShopProductCatalog shopProductCatalog) {
		this.shopProductInventory = shopProductInventory;
		this.shopProductCatalog = shopProductCatalog;

	}


		@Override
		public void initialize() {
			Genre scienceFiction = createGenre("Science Fiction");
			Genre adventure = createGenre("Adventure");
			Genre fiction = createGenre("Fiction");
			Genre cooking = createGenre("Cooking");
			Genre history = createGenre("History");


			shopProductCatalog.save(new Book("The Great Gatsby", "stephen", Money.of(10.99, "EUR"),
				"A novel set in the 1920s about the American Dream", fiction, "F. Scott Fitzgerald",
				"9780743273565", "Scribner"));

			shopProductCatalog.save(new Book("Sapiens: A Brief History of Humankind", "stephen", Money.of(14.99, "EUR"),
				"Explores the history of humankind", history, "Yuval Noah Harari",
				"9780062316110", "Harper"));

			shopProductCatalog.save(new Book("Harry Potter and the Sorcerer's Stone", "stephen", Money.of(8.99, "EUR"),
				"A young wizard's journey begins", adventure, "J.K. Rowling",
				"9780590353427", "Scholastic"));

			shopProductCatalog.save(new Book("The revolution", "stephen", Money.of(12.99, "EUR"),
				"An exploration of cosmology by Stephen Hawking", history, "Stephen Hawking",
				"9780553380163", "Bantam"));

			shopProductCatalog.save (new Merch("T-Shirt", "schmeh", Money.of(19.99, "EUR"), "Comfortable cotton T-shirt"));
			shopProductCatalog.save (new Merch("Mug", "schmeh", Money.of(9.99, "EUR"), "Ceramic coffee mug"));
			shopProductCatalog.save (new Merch("Cap", "schmeh", Money.of(14.99, "EUR"), "Stylish baseball cap"));
			shopProductCatalog.save (new Merch("Poster", "schmeh", Money.of(5.99, "EUR"), "Decorative wall poster"));

			shopProductCatalog.save(new Calendar("Nature 2024", "schmeh", Money.of(12.99, "EUR"),
				"A calendar featuring beautiful nature landscapes for each month."));

			shopProductCatalog.save(new Calendar("Space Exploration 2024", "stephen", Money.of(15.99, "EUR"),
				"Explore the wonders of space with breathtaking images each month."));

			shopProductCatalog.save(new Calendar("Historical Monuments 2024", "schmeh", Money.of(10.99, "EUR"),
				"A calendar showcasing famous historical monuments from around the world."));

			shopProductCatalog.save(new Calendar("Wildlife Photography 2024", "stephen", Money.of(14.99, "EUR"),
				"Each month features stunning photographs of wildlife in their natural habitats."));


			//All products added to a Inventory
			shopProductCatalog.findAll().forEach( (ShopProduct product) -> {
				if (shopProductInventory.findByProduct(product).isEmpty()) {
					shopProductInventory.save(new UniqueInventoryItem(product, Quantity.of(10)));
				}
			});
		}


}

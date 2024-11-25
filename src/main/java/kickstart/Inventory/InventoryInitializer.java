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


			shopProductCatalog.save(new Book("The Great Gatsby", "gatsby", Money.of(10.99, "USD"),
				"A novel set in the 1920s about the American Dream", fiction, "F. Scott Fitzgerald",
				"9780743273565", "Scribner"));

			shopProductCatalog.save(new Book("Sapiens: A Brief History of Humankind", "sapiens", Money.of(14.99, "USD"),
				"Explores the history of humankind", history, "Yuval Noah Harari",
				"9780062316110", "Harper"));

			shopProductCatalog.save(new Book("Harry Potter and the Sorcerer's Stone", "harry", Money.of(8.99, "USD"),
				"A young wizard's journey begins", adventure, "J.K. Rowling",
				"9780590353427", "Scholastic"));

			shopProductCatalog.save(new Book("The revolution", "stephen", Money.of(12.99, "USD"),
				"An exploration of cosmology by Stephen Hawking", history, "Stephen Hawking",
				"9780553380163", "Bantam"));

			shopProductCatalog.save (new Merch("T-Shirt", "t-shirt", Money.of(19.99, "USD"), "Comfortable cotton T-shirt"));
			shopProductCatalog.save (new Merch("Mug", "mug", Money.of(9.99, "USD"), "Ceramic coffee mug"));
			shopProductCatalog.save (new Merch("Cap", "cap", Money.of(14.99, "USD"), "Stylish baseball cap"));
			shopProductCatalog.save (new Merch("Poster", "poster", Money.of(5.99, "USD"), "Decorative wall poster"));

			shopProductCatalog.save(new Calendar("Nature 2024", "nature2024", Money.of(12.99, "USD"),
				"A calendar featuring beautiful nature landscapes for each month."));

			shopProductCatalog.save(new Calendar("Space Exploration 2024", "space", Money.of(15.99, "USD"),
				"Explore the wonders of space with breathtaking images each month."));

			shopProductCatalog.save(new Calendar("Historical Monuments 2024", "history", Money.of(10.99, "USD"),
				"A calendar showcasing famous historical monuments from around the world."));

			shopProductCatalog.save(new Calendar("Wildlife Photography 2024", "wildlife", Money.of(14.99, "USD"),
				"Each month features stunning photographs of wildlife in their natural habitats."));


			//All products added to a Inventory
			shopProductCatalog.findAll().forEach( (ShopProduct product) -> {
				if (shopProductInventory.findByProduct(product).isEmpty()) {
					shopProductInventory.save(new UniqueInventoryItem(product, Quantity.of(10)));
				}
			});
		}


}

package  kickstart.Inventory;

import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InventoryInitializer implements DataInitializer {
		Genre scienceFiction = new Genre("Science Fiction");

		private final UniqueInventory<UniqueInventoryItem> inventory;
		private final BookCatalog bookCatalog; //Book catalog to be able to use the functions of the UniqueItem interface

		InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, BookCatalog bookCatalog){

			this.bookCatalog = bookCatalog;
			this.inventory = inventory;

		}
		// this should be for calendars and merch, but is this the way
		// or should the be left empty?
		Genre general = new Genre("General");

		Genre adventure = new Genre("Adventure");
		Genre fiction = new Genre("Fiction");
		Genre cooking = new Genre("Cooking");
		Genre history = new Genre("History");
		Genre mystery = new Genre("Mystery");

		@Override
			public void initialize() {

			if (bookCatalog.findAll().iterator().hasNext()){
				return;
			}
				bookCatalog.save((new ShopProduct("Calendar 2024", "calendar2024.jpg",
				Money.of(12.99, "USD"), general, ShopProduct.ProductType.CALENDER, 1)));

			bookCatalog.save(new ShopProduct("Science Fiction Book", "scifi_book.jpg",
				Money.of(9.99, "USD"), scienceFiction, ShopProduct.ProductType.BOOK, 2));

			bookCatalog.save(new ShopProduct("Novel", "novel.jpg",
				Money.of(14.99, "USD"), fiction, ShopProduct.ProductType.BOOK, 3));

			bookCatalog.save(new ShopProduct("Adventure Calendar", "adventure_calendar.jpg",
				Money.of(15.00, "USD"), adventure, ShopProduct.ProductType.CALENDER, 4));

			bookCatalog.save(new ShopProduct("Mystery Book", "mystery_book.jpg",
				Money.of(10.99, "USD"), mystery, ShopProduct.ProductType.BOOK, 5));

			bookCatalog.save(new ShopProduct("Band T-Shirt", "band_tshirt.jpg",
				Money.of(20.00, "USD"), general, ShopProduct.ProductType.MERCH, 6));

			bookCatalog.save(new ShopProduct("Cooking Book", "cooking_book.jpg",
				Money.of(18.00, "USD"), cooking, ShopProduct.ProductType.BOOK, 7));

			bookCatalog.save(new ShopProduct("Pop Star Poster", "pop_poster.jpg",
				Money.of(5.99, "USD"), general, ShopProduct.ProductType.MERCH, 8));

			bookCatalog.save(new ShopProduct("History Calendar", "history_calendar.jpg",
				Money.of(11.50, "USD"), history, ShopProduct.ProductType.CALENDER, 9));
			//All products added to a catalog

			bookCatalog.findAll().forEach(ShopProduct -> {

				// Try to find an InventoryItem for the project and create a default one with 10 items if none available
				if (inventory.findByProduct(ShopProduct).isEmpty()) {
					inventory.save(new UniqueInventoryItem(ShopProduct, Quantity.of(10)));
				}
			}); //This thing iterates through the book catalog, and for each product it adds it to the inventory with 10 as the default quantity

		}

}

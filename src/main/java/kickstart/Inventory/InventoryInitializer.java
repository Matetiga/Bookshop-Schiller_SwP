package  kickstart.Inventory;

import kickstart.Inventory.Products.*;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static kickstart.Inventory.Products.Genre.createGenre;

@Component
public class InventoryInitializer implements DataInitializer {
	//added this initialization, because otherwise in the Genre class a new List would be
	// created everytime a new Genre object is created
	private List<Genre> genres;

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final BookCatalog bookCatalog; //Book catalog to be able to use the functions of the UniqueItem interface

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, BookCatalog bookCatalog){

		this.bookCatalog = bookCatalog;
		this.inventory = inventory;
		genres = new ArrayList<>();
	}

	// TODO
	// the inventory controller should be the one to create the inventory
	// that means that the inventory initializer should store the items in tis inventory attribute
	// like in the videoshop example


		@Override
		public void initialize() {
			// this should be for calendars and merch, but is this the way
			// or should the be left empty?
			Genre general = createGenre("General");
			Genre scienceFiction = createGenre("Science Fiction");
			genres.add(general);
			Genre adventure = createGenre("Adventure");
			Genre fiction = createGenre("Fiction");
			Genre cooking = createGenre("Cooking");
			Genre history = createGenre("History");
			Genre mystery = createGenre("Mystery");

			if (bookCatalog.findAll().iterator().hasNext()){
				return;
			}
				bookCatalog.save((new Calendar("Calendar 2024", "calendar2024.jpg",
				Money.of(12.99, "USD"),  "Hola")));

			bookCatalog.save(new Book("Science Fiction Book", "scifi_book.jpg",
				Money.of(9.99, "USD"),  "des", scienceFiction,"Una vieja", "123", "GANDALF!?"));

			bookCatalog.save(new Book("Novel", "novel.jpg",
				Money.of(14.99, "USD"), "des", scienceFiction,"Dos viejas", "123", "Terminator"));

			bookCatalog.save(new Calendar("Adventure Calendar", "adventure_calendar.jpg",
				Money.of(15.00, "USD"), "Chao"));

			bookCatalog.save(new Merch("Band T-Shirt", "band_tshirt.jpg",
				Money.of(20.00, "USD"),  "Hola"));

			//All products added to a catalog

			bookCatalog.findAll().forEach(ShopProduct -> {

				// Try to find an InventoryItem for the project and create a default one with 10 items if none available
				if (inventory.findByProduct(ShopProduct).isEmpty()) {
					inventory.save(new UniqueInventoryItem(ShopProduct, Quantity.of(10)));
				}
			}); //This thing iterates through the book catalog, and for each product it adds it to the inventory with 10 as the default quantity

		}

}

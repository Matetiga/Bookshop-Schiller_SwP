package  kickstart.Inventory;

import kickstart.Inventory.Products.*;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import static kickstart.Inventory.Products.Genre.createGenre;
import static org.hibernate.id.SequenceMismatchStrategy.LOG;

@Component
public class InventoryInitializer implements DataInitializer {
	//added this initialization, because otherwise in the Genre class a new List would be
	// created everytime a new Genre object is created

	private final UniqueInventory<UniqueInventoryItem> bookInventory;
	private final UniqueInventory<UniqueInventoryItem> calendarInventory;
	private final UniqueInventory<UniqueInventoryItem> merchInventory;
	private final BookCatalog bookCatalog; //Book catalog to be able to use the functions of the UniqueItem interface
	private final MerchCatalog merchCatalog;
	private final CalendarCatalog calendarCatalog;

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> bookInventory, UniqueInventory<UniqueInventoryItem> calendarInventory, UniqueInventory<UniqueInventoryItem> merchInventory,
						 BookCatalog bookCatalog, MerchCatalog merchCatalog, CalendarCatalog calendarCatalog){

		this.bookInventory = bookInventory;
		this.calendarInventory = calendarInventory;
		this.merchInventory = merchInventory;
		this.bookCatalog = bookCatalog;
		this.merchCatalog = merchCatalog;
		this.calendarCatalog = calendarCatalog;
	}


		@Override
		public void initialize() {
			// this should be for calendars and merch, but is this the way
			// or should the be left empty?
			Genre general = createGenre("General");
			Genre scienceFiction = createGenre("Science Fiction");
			Genre adventure = createGenre("Adventure");
			Genre fiction = createGenre("Fiction");
			Genre cooking = createGenre("Cooking");
			Genre history = createGenre("History");
			Genre mystery = createGenre("Mystery");


//			if (bookCatalog.findAll().iterator().hasNext()){
//				return;
//			}
//			if (merchCatalog.findAll().iterator().hasNext()) {
//				return;
//			}
//			if (calendarCatalog.findAll().iterator().hasNext()) {
//				return;
//			}
			bookCatalog.save(new Book("The Great Gatsby", "gatsby.jpg", Money.of(10.99, "USD"),
				"A novel set in the 1920s about the American Dream", fiction, "F. Scott Fitzgerald",
				"9780743273565", "Scribner"));

			bookCatalog.save(new Book("Sapiens: A Brief History of Humankind", "sapiens.jpg", Money.of(14.99, "USD"),
				"Explores the history of humankind", history, "Yuval Noah Harari",
				"9780062316110", "Harper"));

			bookCatalog.save(new Book("Harry Potter and the Sorcerer's Stone", "hp1.jpg", Money.of(8.99, "USD"),
				"A young wizard's journey begins", adventure, "J.K. Rowling",
				"9780590353427", "Scholastic"));

			bookCatalog.save(new Book("The revolution", "the_revolution.jpg", Money.of(12.99, "USD"),
				"An exploration of cosmology by Stephen Hawking", history, "Stephen Hawking",
				"9780553380163", "Bantam"));

			merchCatalog.save (new Merch("T-Shirt", "image1.jpg", Money.of(19.99, "USD"), "Comfortable cotton T-shirt"));
			merchCatalog.save (new Merch("Mug", "image2.jpg", Money.of(9.99, "USD"), "Ceramic coffee mug"));
			merchCatalog.save (new Merch("Cap", "image3.jpg", Money.of(14.99, "USD"), "Stylish baseball cap"));
			merchCatalog.save (new Merch("Poster", "image4.jpg", Money.of(5.99, "USD"), "Decorative wall poster"));

			calendarCatalog.save(new Calendar("Nature 2024", "nature_calendar.jpg", Money.of(12.99, "USD"),
				"A calendar featuring beautiful nature landscapes for each month."));

			calendarCatalog.save(new Calendar("Space Exploration 2024", "space_calendar.jpg", Money.of(15.99, "USD"),
				"Explore the wonders of space with breathtaking images each month."));

			calendarCatalog.save(new Calendar("Historical Monuments 2024", "monuments_calendar.jpg", Money.of(10.99, "USD"),
				"A calendar showcasing famous historical monuments from around the world."));

			calendarCatalog.save(new Calendar("Wildlife Photography 2024", "wildlife_calendar.jpg", Money.of(14.99, "USD"),
				"Each month features stunning photographs of wildlife in their natural habitats."));


			//All products added to a catalog
			// TODO is there a better way to do this?
			bookCatalog.findAll().forEach(Book -> {
				// Try to find an InventoryItem for the project and create a default one with 10 items if none available
				if (bookInventory.findByProduct(Book).isEmpty()) {
					bookInventory.save(new UniqueInventoryItem(Book, Quantity.of(10)));
				}
			}); //This thing iterates through the book catalog, and for each product it adds it to the inventory with 10 as the default quantity

			merchCatalog.findAll().forEach(Merch -> {
				if (merchInventory.findByProduct(Merch).isEmpty()) {
					merchInventory.save(new UniqueInventoryItem(Merch, Quantity.of(10)));
				}
			});

			calendarCatalog.findAll().forEach(Calendar -> {
				if (calendarInventory.findByProduct(Calendar).isEmpty()) {
					calendarInventory.save(new UniqueInventoryItem(Calendar, Quantity.of(10)));
				}
			});

		}


}

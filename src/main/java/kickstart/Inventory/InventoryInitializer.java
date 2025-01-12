package  kickstart.Inventory;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
			Genre militaryStrategy = createGenre("Military Strategy");
			Genre crimeNovel = createGenre("Crime Novel");
			Genre nonfiction = createGenre("Nonfiction");

			Set<Genre> genreSet1 = new HashSet<>(Set.of(fiction, cooking));
			Set<Genre> genreSet2 = new HashSet<>(Set.of(history));
			Set<Genre> genreSet3 = new HashSet<>(Set.of(adventure));
			Set<Genre> genreSet4 = new HashSet<>(Set.of(scienceFiction));
			Set<Genre> genreSet5 = new HashSet<>(Set.of(fiction,adventure));
			Set<Genre> genreSet6 = new HashSet<>(Set.of(militaryStrategy));
			Set<Genre> genreSet7 = new HashSet<>(Set.of(crimeNovel));
			Set<Genre> genreSet8 = new HashSet<>(Set.of(nonfiction));
			Set<Genre> genreSet9 = new HashSet<>(Set.of(cooking));

			shopProductCatalog.save(new Book("The Great Gatsby", "/resources/img/cover/gatsby.jpg", Money.of(10.99, "EUR"),
				"A novel set in the 1920s about the American Dream", genreSet1, "F. Scott Fitzgerald",
				"9780743273565", "Scribner"));

			shopProductCatalog.save(new Book("Sapiens: A Brief History of Humankind", "/resources/img/cover/sapiens.jpg", Money.of(14.99, "EUR"),
				"Explores the history of humankind", genreSet2, "Yuval Noah Harari",
				"9780062316110", "Harper"));

			shopProductCatalog.save(new Book("Harry Potter and the Sorcerer's Stone", "/resources/img/cover/harry.jpg", Money.of(8.99, "EUR"),
				"A young wizard's journey begins", genreSet3, "J.K. Rowling",
				"9780590353427", "Scholastic"));

			shopProductCatalog.save(new Book("The revolution", "/resources/img/cover/stephen.jpg", Money.of(12.99, "EUR"),
				"An exploration of cosmology by Stephen Hawking", genreSet2, "Stephen Hawking",
				"9780553380163", "Bantam"));

			shopProductCatalog.save(new Book("The Lord of The Rings", "/resources/img/cover/lotr.jpg", Money.of(17.99,"EUR"),
				"An epic quest to destroy a powerful ring",genreSet3,"J.R.R Tolkien","9780544003415","George Allen"));

			shopProductCatalog.save(new Book("Ender's Game", "/resources/img/cover/endersgame.jpg", Money.of(12.99,"EUR"),
				"Exciting Book playing in the future",genreSet4,"Orson Scott Card","9780812550702","Tor Books"));

			shopProductCatalog.save(new Book("The Guns of August", "/resources/img/cover/tgoa.jpg", Money.of(20.99,"EUR"),
				"The first month of World War I",genreSet2,"Barbara W. Tuchman","9780345476098","Presidio Press"));

			shopProductCatalog.save(new Book("Moby Dick", "/resources/img/cover/mobydick.jpg", Money.of(14.99,"EUR"),
				"A gripping tale of obsession as a captain hunt the legendary white whale, Moby Dick",genreSet5,"Herman Melville",
				"9781503280786","CreateSpace Independent Publishing Platform"));

			shopProductCatalog.save(new Book("The Art of War", "/resources/img/cover/artofwar.jpg", Money.of(6.99,"EUR"),
				"Timeless strategies and wisdom on warfare, leadership and strategy by Sun Tzu",genreSet6,"Sun Tzu","9781721195091","CreateSpace Independent Publishing Platform"));

			shopProductCatalog.save(new Book("The Godfather", "/resources/img/cover/godfather.jpg", Money.of(29.99,"EUR"),
				"A powerful saga of crime, family, and loyalty in the world of the Mafia",genreSet7,"Mario Puzo","9780099528128","Arrow"));

			shopProductCatalog.save(new Book("Java for Dummies", "/resources/img/cover/javafordummies.jpg", Money.of(7.99,"EUR"),
				"Learn Java",genreSet8,"Barry Burd","9781603095082","Wiley-VCH"));

			Book snoopBook = (new Book("From Crook To Cook: Platinum Recipes From Tha Boss Dogg's Kitchen", "/resources/img/cover/snoopdogg.jpg",
				Money.of(99.99,"EUR"),
				"Best Recipes world wide",genreSet9,"Snoop Dogg",
				"9781452179612","Chronicle Books"));
			shopProductCatalog.save(snoopBook);

			shopProductCatalog.save (new Merch("T-Shirt", "/resources/img/cover/t-shirt.jpg", Money.of(19.99, "EUR"), "Comfortable cotton T-shirt"));
			shopProductCatalog.save (new Merch("Mug", "/resources/img/cover/mug.jpg", Money.of(9.99, "EUR"), "Ceramic coffee mug"));
			shopProductCatalog.save (new Merch("Cap", "/resources/img/cover/cap.jpg", Money.of(14.99, "EUR"), "Stylish baseball cap"));
			shopProductCatalog.save (new Merch("Poster", "/resources/img/cover/poster.jpg", Money.of(5.99, "EUR"), "Decorative wall poster"));

			shopProductCatalog.save(new Calendar("Nature 2025", "/resources/img/cover/nature2025.jpg", Money.of(12.99, "EUR"),
				"A calendar featuring beautiful nature landscapes for each month."));

			shopProductCatalog.save(new Calendar("Space Exploration 2025", "/resources/img/cover/space2025.jpg", Money.of(15.99, "EUR"),
				"Explore the wonders of space with breathtaking images each month."));

			shopProductCatalog.save(new Calendar("Historical Monuments 2025", "/resources/img/cover/history2025.jpg", Money.of(10.99, "EUR"),
				"A calendar showcasing famous historical monuments from around the world."));

			shopProductCatalog.save(new Calendar("Wildlife Photography 2025", "/resources/img/cover/wildlife2025.jpg", Money.of(14.99, "EUR"),
				"Each month features stunning photographs of wildlife in their natural habitats."));


			//All products added to a Inventory
			shopProductCatalog.findAll().forEach( (ShopProduct product) -> {
				if (shopProductInventory.findByProduct(product).isEmpty()) {
					shopProductInventory.save(new UniqueInventoryItem(product, Quantity.of(10)));
				}
			});

			// so snoop dogg's book has 420 copies
			shopProductInventory.findByProductIdentifier(snoopBook.getProductId())
				.ifPresent(item -> item.increaseQuantity(Quantity.of(410)));
		}


}

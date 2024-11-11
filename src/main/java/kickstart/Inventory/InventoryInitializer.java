package  kickstart.Inventory;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InventoryInitializer implements DataInitializer {
		// this list should be displayed in the inventory
//		private final BookCatalog bookCatalog;
//		Catalog
		ArrayList<ShopProduct> mockProducts = new ArrayList<>();
		Genre scienceFiction = new Genre("Science Fiction");

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

			mockProducts.add(new ShopProduct("Calendar 2024", "calendar2024.jpg",
				Money.of(12.99, "USD"), general, ShopProduct.ProductType.CALENDER, 1));

			mockProducts.add(new ShopProduct("Science Fiction Book", "scifi_book.jpg",
				Money.of(9.99, "USD"), scienceFiction, ShopProduct.ProductType.BOOK, 2));

			mockProducts.add(new ShopProduct("Novel", "novel.jpg",
				Money.of(14.99, "USD"), fiction, ShopProduct.ProductType.BOOK, 3));

			mockProducts.add(new ShopProduct("Adventure Calendar", "adventure_calendar.jpg",
				Money.of(15.00, "USD"), adventure, ShopProduct.ProductType.CALENDER, 4));

			mockProducts.add(new ShopProduct("Mystery Book", "mystery_book.jpg",
				Money.of(10.99, "USD"), mystery, ShopProduct.ProductType.BOOK, 5));

			mockProducts.add(new ShopProduct("Band T-Shirt", "band_tshirt.jpg",
				Money.of(20.00, "USD"), general, ShopProduct.ProductType.MERCH, 6));

			mockProducts.add(new ShopProduct("Cooking Book", "cooking_book.jpg",
				Money.of(18.00, "USD"), cooking, ShopProduct.ProductType.BOOK, 7));

			mockProducts.add(new ShopProduct("Pop Star Poster", "pop_poster.jpg",
				Money.of(5.99, "USD"), general, ShopProduct.ProductType.MERCH, 8));

			mockProducts.add(new ShopProduct("History Calendar", "history_calendar.jpg",
				Money.of(11.50, "USD"), history, ShopProduct.ProductType.CALENDER, 9));
		}

}

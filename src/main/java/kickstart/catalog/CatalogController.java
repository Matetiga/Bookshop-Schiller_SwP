package kickstart.catalog;
import kickstart.Inventory.*;
import kickstart.Inventory.Calendar;
import kickstart.orders.OrderController;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CatalogController {
	private static final Quantity NONE = Quantity.of(0);

	private final ShopProductCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;


	CatalogController(ShopProductCatalog productCatalog, UniqueInventory<UniqueInventoryItem> inventory) {

		this.catalog = productCatalog;
		this.inventory = inventory;
	}

	@GetMapping("/books")
	String bookCatalog(Model model, @RequestParam(value = "genres", required = false) List<String> selectedGenres,
					   @RequestParam(value = "sort", required = false) String sort,
					   @RequestParam(value = "priceRange", required = false) String priceRange,
					   @RequestParam(value = "search", required = false) String search) {

		List<Book> allBooks = inventory.findAll().stream()
			.map(UniqueInventoryItem::getProduct)
			.filter(product -> product instanceof Book)
			.map(product -> (Book) product)
			.toList();

		Set<String> allGenres = allBooks.stream()
			// merge all genres streams into one set
			.flatMap(book -> book.getBookGenres().stream())
			.map(Genre::getGenre)
			.collect(Collectors.toSet());

		// Filter Books by Genre
		List<Book> filteredBooks = selectedGenres == null || selectedGenres.isEmpty()
			? allBooks
			: allBooks.stream()
			.filter(book -> book.getBookGenres().stream()
					.map(Genre::getGenre)
					.anyMatch(selectedGenres::contains)) //should match any genre in selectedGenres
			.toList();

		// Sorting book in ascending or descending order
		if("asc".equalsIgnoreCase(sort)) {
			filteredBooks = filteredBooks.stream()
				.sorted(Comparator.comparing(Book::getPrice))
				.toList();
		} else if("desc".equalsIgnoreCase(sort)) {
			filteredBooks = filteredBooks.stream()
				.sorted(Comparator.comparing(Book::getPrice).reversed())
				.toList();
		}

		// Filter books cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getPrice().getNumber().doubleValue() < 10)
				.toList();
		} else if ("over15".equalsIgnoreCase(priceRange)) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getPrice().getNumber().doubleValue() > 15)
				.toList();
		}

		// Search function by book's title
		if(search != null && !search.trim().isEmpty()) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getName().toLowerCase().contains(search.toLowerCase()))
				.toList();
		}

		model.addAttribute("catalog", filteredBooks);
		model.addAttribute("genres", allGenres);
		model.addAttribute("selectedGenres", selectedGenres);
		model.addAttribute("title", "catalog.book.title");
		model.addAttribute("sort", sort);
		model.addAttribute("priceRange", priceRange);
		model.addAttribute("search", search);

		return "catalog_books";
	}

	@GetMapping("/merch")
	String merchCatalog(Model model,@RequestParam(value = "sort", required = false) String sort,
						@RequestParam(value = "priceRange", required = false) String priceRange,
						@RequestParam(value = "search", required = false) String search) {

		List<Merch> catalog = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Merch){
				catalog.add((Merch) item.getProduct());
			}
		}

		// Sorting merch in ascending or descending order
		if("asc".equalsIgnoreCase(sort)) {
			catalog = catalog.stream()
				.sorted(Comparator.comparing(Merch::getPrice))
				.toList();
		} else if("desc".equalsIgnoreCase(sort)) {
			catalog = catalog.stream()
				.sorted(Comparator.comparing(Merch::getPrice).reversed())
				.toList();
		}

		// Filter merch cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(merch -> merch.getPrice().getNumber().doubleValue() < 10)
				.toList();
		} else if ("over15".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(merch -> merch.getPrice().getNumber().doubleValue() > 15)
				.toList();
		}

		// Search function by merch's title
		if(search != null && !search.trim().isEmpty()) {
			catalog = catalog.stream()
				.filter(merch -> merch.getName().toLowerCase().contains(search.toLowerCase()))
				.toList();
		}

		model.addAttribute("catalog", catalog);
		model.addAttribute("title", "catalog.merch.title");
		model.addAttribute("sort", sort);
		model.addAttribute("priceRange", priceRange);
		model.addAttribute("search", search);

		return "catalog_merch";
	}

	@GetMapping("/calenders")
	String calenderCatalog(Model model,@RequestParam(value = "sort", required = false) String sort,
						   @RequestParam(value = "priceRange", required = false) String priceRange,
						   @RequestParam(value = "search", required = false) String search) {

		List<Calendar> catalog = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Calendar){
				catalog.add((Calendar) item.getProduct());
			}
		}

		// Sorting calendars in ascending or descending order
		if("asc".equalsIgnoreCase(sort)) {
			catalog = catalog.stream()
				.sorted(Comparator.comparing(Calendar::getPrice))
				.toList();
		} else if("desc".equalsIgnoreCase(sort)) {
			catalog = catalog.stream()
				.sorted(Comparator.comparing(Calendar::getPrice).reversed())
				.toList();
		}

		// Filter calendar cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(calendar -> calendar.getPrice().getNumber().doubleValue() < 10)
				.toList();
		} else if ("over15".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(calendar -> calendar.getPrice().getNumber().doubleValue() > 15)
				.toList();
		}

		// Search function by calendar's title
		if(search != null && !search.trim().isEmpty()) {
			catalog = catalog.stream()
				.filter(merch -> merch.getName().toLowerCase().contains(search.toLowerCase()))
				.toList();
		}

		model.addAttribute("catalog", catalog);
		model.addAttribute("title", "catalog.calender.title");
		model.addAttribute("sort", sort);
		model.addAttribute("priceRange", priceRange);
		model.addAttribute("search", search);

		return "catalog_calender";
	}

	@GetMapping("/book/{bookProduct}")
	String bookDetail(@PathVariable ShopProduct bookProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(bookProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("book", bookProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_book";
	}

	@GetMapping("/merch/{merchProduct}")
	String merchDetail(@PathVariable ShopProduct merchProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(merchProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("merch", merchProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_merch";
	}

	@GetMapping("/calender/{calenderProduct}")
	String calenderDetail(@PathVariable ShopProduct calenderProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(calenderProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("calender", calenderProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_calender";
	}


}

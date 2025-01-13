package kickstart.catalog;
import kickstart.Inventory.*;
import kickstart.Inventory.Calendar;
import kickstart.Achievement.Achievement;
import kickstart.user.User;
import kickstart.user.UserManagement;
import kickstart.Service.UserAchievementService;
import kickstart.orders.OrderController;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.useraccount.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	private final UserManagement userManagement;
	private final UserAchievementService userAchievementService;


	CatalogController(ShopProductCatalog productCatalog, UniqueInventory<UniqueInventoryItem> inventory, UserManagement userManagement) {

		this.catalog = productCatalog;
		this.inventory = inventory;
		this.userManagement = userManagement;
		this.userAchievementService = new UserAchievementService(this.userManagement);
	}

	@GetMapping("/books")
	String bookCatalog(Model model, @RequestParam(value = "genres", required = false) List<String> selectedGenres,
					   @RequestParam(value = "sort", required = false) String sort,
					   @RequestParam(value = "priceRange", required = false) String priceRange,
					   @RequestParam(value = "search", required = false) String search) {


		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if(principle instanceof UserDetails) {
			userDetails = (UserDetails) principle;
		} else {
			model.addAttribute("userName","Gast");
		}

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

		Achievement ach1 = new Achievement("Luxus für Lau!","Du hast im Katalog nach 'unter 10€' gefiltert", Role.of("CUSTOMER"));
		// Filter books cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getPrice().getNumber().doubleValue() < 10)
				.toList();
			if(userDetails != null) {
				userAchievementService.processAchievement(userDetails, ach1, model);
			}

		} else if ("over25".equalsIgnoreCase(priceRange)) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getPrice().getNumber().doubleValue() > 25)
				.toList();
		} else if ("10to25".equalsIgnoreCase(priceRange)) {
			filteredBooks = filteredBooks.stream().
				filter(book -> book.getPrice().getNumber().doubleValue() > 10 && book.getPrice().getNumber().doubleValue() < 25)
				.toList();
		}

		Achievement ach2 = new Achievement("Hier ist nichts!", "Du siehst keine Bücher.", Role.of("CUSTOMER"));
		// Search function by book's title
		if(search != null && !search.trim().isEmpty()) {
			filteredBooks = filteredBooks.stream()
				.filter(book -> book.getName().toLowerCase().contains(search.toLowerCase()))
				.toList();
		}

		if(filteredBooks.isEmpty()) {
			userAchievementService.processAchievement(userDetails,ach2,model);
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

		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if(principle instanceof UserDetails) {
			userDetails = (UserDetails) principle;
		} else {
			model.addAttribute("userName","Gast");
		}


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

		Achievement ach1 = new Achievement("Luxus für Lau!","Du hast im Katalog nach 'unter 10€' gefiltert", Role.of("CUSTOMER"));
		// Filter merch cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(merch -> merch.getPrice().getNumber().doubleValue() < 10)
				.toList();
			if(userDetails != null) {
				userAchievementService.processAchievement(userDetails, ach1, model);
			}
		} else if ("over25".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(merch -> merch.getPrice().getNumber().doubleValue() > 25)
				.toList();
		} else if ("10to25".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream().
				filter(book -> book.getPrice().getNumber().doubleValue() > 10 && book.getPrice().getNumber().doubleValue() < 25)
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

		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		if(principle instanceof UserDetails) {
			userDetails = (UserDetails) principle;
		} else {
			model.addAttribute("userName","Gast");
		}

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

		Achievement ach1 = new Achievement("Luxus für Lau!","Du hast im Katalog nach 'unter 10€' gefiltert", Role.of("CUSTOMER"));
		// Filter calendar cheaper than 10€ or more expensive than 15€
		if("under10".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(calendar -> calendar.getPrice().getNumber().doubleValue() < 10)
				.toList();

			if(userDetails != null) {
				userAchievementService.processAchievement(userDetails, ach1, model);
			}
		} else if ("over25".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream()
				.filter(calendar -> calendar.getPrice().getNumber().doubleValue() > 25)
				.toList();
		} else if ("10to25".equalsIgnoreCase(priceRange)) {
			catalog = catalog.stream().
				filter(book -> book.getPrice().getNumber().doubleValue() > 10 && book.getPrice().getNumber().doubleValue() < 25)
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
	public String bookDetail(@PathVariable ShopProduct bookProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(bookProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("book", bookProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_book";
	}

	@GetMapping("/merch/{merchProduct}")
	public String merchDetail(@PathVariable ShopProduct merchProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(merchProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("merch", merchProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_merch";
	}

	@GetMapping("/calender/{calenderProduct}")
	public String calenderDetail(@PathVariable ShopProduct calenderProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(calenderProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("calender", calenderProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_calender";
	}


}

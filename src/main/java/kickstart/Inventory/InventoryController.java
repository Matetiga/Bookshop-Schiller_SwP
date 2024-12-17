package kickstart.Inventory;

import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InventoryController {
	@ManyToOne
	private final UniqueInventory<UniqueInventoryItem> shopProductInventory;
	private final ShopProductCatalog shopProductCatalog;


	InventoryController(UniqueInventory<UniqueInventoryItem> shopProductInventory, ShopProductCatalog shopProductCatalog) {
		this.shopProductInventory = shopProductInventory;
		this.shopProductCatalog = shopProductCatalog;
	}

	@PostMapping("/inventory/add_newGenre")
	public String addNewGenre(@NotBlank @RequestParam("newGenre") String genre, Model model) {
		try {
			Genre.createGenre(genre);

		} catch (IllegalArgumentException e) {
			model.addAttribute("error_newGenre", "Genre can not be empty");
		}
		showInventory(model);
		return "inventory_book";
	}

	@PostMapping("/inventory/delete_genre")
	public String deleteGenre(@RequestParam("genre") String genre, Model model) {
		// this will get the already existing genre
		Genre deleteGenre = Genre.createGenre(genre);

		Genre.deleteGenre(deleteGenre);
		shopProductCatalog.findAll().stream()
			.filter(product -> product instanceof Book)
			.map(product -> (Book) product)
			.filter(book -> book.getBookGenres().contains(deleteGenre))
			.forEach(book -> {
				book.getBookGenres().remove(deleteGenre);
				shopProductCatalog.save(book);
			});
		showInventory(model);
		return "inventory_book";
	}

	@GetMapping("/inventory_book")
	public String showInventory(Model model) {
		List<Map.Entry<Book, Quantity>> books = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Book) {
				books.add(new AbstractMap.SimpleEntry<Book, Quantity>((Book) item.getProduct(), item.getQuantity()));
			}
		}

		model.addAttribute("bookGenres_addBook", Genre.getAllGenres());
		model.addAttribute("books", books);
		model.addAttribute("viewName", "inventory_book");
		// this prevents the form from being overwritten when the page is re rendered
		if (!model.containsAttribute("bookForm")) {
			model.addAttribute("bookForm", new AddBookForm());
		}
		return "inventory_book";
	}

	@GetMapping("/inventory_merch")
	public String showMerchInventory(Model model) {
		List<Map.Entry<Merch, Quantity>> merch = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Merch) {
				merch.add(new AbstractMap.SimpleEntry<Merch, Quantity>((Merch) item.getProduct(), item.getQuantity()));
			}
		}

		model.addAttribute("merch", merch);
		model.addAttribute("viewName", "inventory_merch");

		if (!model.containsAttribute("merchForm")) {
			model.addAttribute("merchForm", new AddMerchCalendarForm());
		}

		return "inventory_merch";
	}

	@GetMapping("/inventory_calendar")
	public String showCalendarInventory(Model model) {
		List<Map.Entry<Calendar, Quantity>> calendars = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Calendar) {
				calendars.add(new AbstractMap.SimpleEntry<Calendar, Quantity>((Calendar) item.getProduct(), item.getQuantity()));
			}
		}

		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}

		model.addAttribute("calendars", calendars);
		model.addAttribute("viewName", "inventory_calendar");

		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}
		return "inventory_calendar";
	}


	@PostMapping("/inventory/increase")
	public String increaseProductQuantity(@RequestParam("itemId") Product.ProductIdentifier id,
										  @RequestParam("viewName") String viewName, Model model) {
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.increaseQuantity(Quantity.of(1));
			shopProductInventory.save(item);
		});
		if (viewName.equals("inventory_book")) {
			showInventory(model);
		}
		if (viewName.equals("inventory_calendar")) {
			showCalendarInventory(model);
		}
		if (viewName.equals("inventory_merch")) {
			showMerchInventory(model);
		}
		return viewName;

	}

	@PostMapping("/inventory/decrease")
	public String decreaseProductQuantity(@RequestParam("itemId") Product.ProductIdentifier id, Model model) {
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();

		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.increaseQuantity(Quantity.of(-1));
			shopProductInventory.save(item);
		});

		if (shopProduct.getProduct() instanceof Book) {
			showInventory(model);
			return "inventory_book";
		}
		if (shopProduct.getProduct() instanceof Calendar) {
			showCalendarInventory(model);
			return "redirect:/inventory_calendar";
		}
		if (shopProduct.getProduct() instanceof Merch) {
			showMerchInventory(model);
			return "redirect:/inventory_merch";
		} else {
			return "inventory_book";
		}

	}


	@PostMapping("/inventory/delete")
	public String deleteProduct(@RequestParam("itemId") Product.ProductIdentifier id, Model model) {
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();
		shopProductInventory.findByProductIdentifier(id).ifPresent(shopProductInventory::delete);
		if (shopProduct.getProduct() instanceof Book) {
			showInventory(model);
			return "inventory_book";
		}
		if (shopProduct.getProduct() instanceof Calendar) {
			showCalendarInventory(model);
			return "redirect:/inventory_calendar";
		}
		if (shopProduct.getProduct() instanceof Merch) {
			showMerchInventory(model);
			return "redirect:/inventory_merch";
		} else {
			return "inventory_book";
		}

	}


	public String saveImage(MultipartFile image) {
		String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

		Path imagePath = Paths.get("uploads/images", fileName);
		try {
			Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "../" + imagePath.toString();
	}

	public boolean isValidAndUniqueISBN(String ISBN, Product.ProductIdentifier id) {
		if (ISBN == null || !ISBN.matches("\\d{13}")) {//d 0-9
			return false;
		}

		// check correct format
		// odd position digits * 1
		// even position digits * 3
		// sum of all digits must be = 0 (mod 10)
		int total = 0;
		for (int i = 0; i < 12; i++) { // First 12 digits only
			int digit = Character.getNumericValue(ISBN.charAt(i));
			if (i % 2 == 0) { // for odd (starting at 0) position
				total += digit;
			} else {
				total += 3 * digit;
			}
		}

		// Calculate check digit
		int checkDigit = (10 - (total % 10)) % 10;

		// Check uniqueness
		return shopProductCatalog.findAll().stream() //how you iterate through some collections
			.filter(product -> product instanceof Book) //only get instances of book
			.map(product -> (Book) product) //Cast each product to access the methods in book
			.noneMatch(book -> book.getISBN().equals(ISBN) &&
				(id == null || !book.getId().equals(id))) //Exclude when creating a new book or when updating a book
		&& checkDigit == Character.getNumericValue(ISBN.charAt(12)); // Compare check digit to the 13th digit
	}

	@PostMapping("/inventory/add_book")
	public String addBook(@Valid AddBookForm bookForm, BindingResult result,
						  @RequestParam("imageFile") MultipartFile file, Model model) {

		if (result.hasErrors() || !isValidAndUniqueISBN(bookForm.getISBN(), null)){
			if(!isValidAndUniqueISBN(bookForm.getISBN(), null)){
				model.addAttribute("errorDuplicated_Isbn", "ISBN must be unique and 13 digits long");
			}
			showInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_book";
		}

		String imagePath = saveImage(file);

		Book book = new Book(bookForm.getName(),
			imagePath,
			Money.of(bookForm.getPrice(), "EUR"),
			bookForm.getDescription(),
			bookForm.getGenre().stream()
				.map(Genre::createGenre)
				.collect(Collectors.toSet()),
			bookForm.getAuthor(),
			bookForm.getISBN(),
			bookForm.getPublisher());
		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem(book, Quantity.of(bookForm.getStock())));
		showInventory(model);

		return "inventory_book";
	}

	@PostMapping("/inventory/add_merch")
	public String addMerch(@Valid @ModelAttribute("merchForm") AddMerchCalendarForm merchForm, BindingResult result,
						   @RequestParam("imageFile") MultipartFile file, Model model) {
		if (result.hasErrors()) {
			showMerchInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_merch";
		}

		String imagePath = saveImage(file);
		Merch merch = new Merch(merchForm.getName(),
			imagePath,
			Money.of(merchForm.getPrice(), "EUR"),
			merchForm.getDescription());
		shopProductCatalog.save(merch);
		shopProductInventory.save(new UniqueInventoryItem(merch, Quantity.of(merchForm.getStock())));
		showMerchInventory(model);
		return "inventory_merch";
	}

	@PostMapping("/inventory/add_calendar")
	public String addCalendar(@Valid @ModelAttribute("calendarForm") AddMerchCalendarForm calendarForm, BindingResult result,
							  @RequestParam("imageFile") MultipartFile file, Model model) {
		if (result.hasErrors()) {
			showCalendarInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_calendar";
		}
		String imagePath = saveImage(file);
		Calendar calendar = new Calendar(calendarForm.getName(),
			imagePath,
			Money.of(calendarForm.getPrice(), "EUR"),
			calendarForm.getDescription());

		shopProductCatalog.save(calendar);
		shopProductInventory.save(new UniqueInventoryItem(calendar, Quantity.of(calendarForm.getStock())));
		showCalendarInventory(model);
		return "inventory_calendar";
	}


	// This method should give all the information of the different products to the html
	// depending on its class
	@GetMapping("/inventory/editable/{itemId}")
	public String getDetail(@PathVariable Product.ProductIdentifier itemId, Model model) {

		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(itemId).get();
		model.addAttribute("productId", shopProduct.getProduct().getId());
		if (shopProduct.getProduct() instanceof Book) {

			model.addAttribute("bookGenres", Genre.getAllGenres());
			Book book = (Book) shopProduct.getProduct();
			Set<String> set = book.getBookGenres().stream()
				.map(Genre::getGenre)
				.collect(Collectors.toSet());

			AddBookForm form = new AddBookForm(
				book.getName(),
				book.getImage(),
				book.getDescription(),
				set,
				book.getAuthor(),
				book.getISBN(),
				book.getPublisher(),
				book.getPrice().getNumber().doubleValue(),
				shopProduct.getQuantity().getAmount().intValue()
			);
			model.addAttribute("bookForm", form);

		}
		if (shopProduct.getProduct() instanceof Calendar) {
			Calendar calendar = (Calendar) shopProduct.getProduct();
			AddMerchCalendarForm form = new AddMerchCalendarForm(
				calendar.getName(),
				calendar.getImage(),
				calendar.getPrice().getNumber().doubleValue(),
				calendar.getDescription(),
				shopProduct.getQuantity().getAmount().intValue()
			);
			model.addAttribute("calendarForm", form);
		}
		if (shopProduct.getProduct() instanceof Merch) {
			Merch merch = (Merch) shopProduct.getProduct();
			AddMerchCalendarForm form = new AddMerchCalendarForm(
				merch.getName(),
				merch.getImage(),
				merch.getPrice().getNumber().doubleValue(),
				merch.getDescription(),
				shopProduct.getQuantity().getAmount().intValue()
			);
			model.addAttribute("merchForm", form);
		}

		return "inventory_editable";
	}

	private void setStockEdit(UniqueInventoryItem item, Quantity oldQuantity, long newQuantity) {
		long oldStock = oldQuantity.getAmount().longValue();

		if (oldStock < newQuantity) {
			item.increaseQuantity(Quantity.of(newQuantity - oldStock));
		}
		if (oldStock > newQuantity) {
			item.decreaseQuantity(Quantity.of(oldStock - newQuantity));
		}
	}


	@PostMapping("/inventory/save_book/{itemId}")
	public String saveBook(@Valid AddBookForm bookForm, BindingResult result,
						   @RequestParam("imageFile") MultipartFile file,
						   @PathVariable Product.ProductIdentifier itemId, Model model) {

		if (result.hasErrors() || !isValidAndUniqueISBN(bookForm.getISBN(), itemId)) {
			if (!isValidAndUniqueISBN(bookForm.getISBN(), itemId)) {
				model.addAttribute("errorDuplicated_Isbn", "ISBN must be unique and 13 digits long");
			}
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}
		// this should update the path of the image if a new image is uploaded
		// otherwise keep the old path
		String imagePath;
		if (!file.isEmpty()) {
			imagePath = saveImage(file);
		} else {
			imagePath = bookForm.getImage();
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Book book = (Book) item.getProduct();
			book.setName(bookForm.getName());
			book.setImage(imagePath);
			book.setPrice(Money.of(bookForm.getPrice(), "EUR"));
			book.setDescription(bookForm.getDescription());
			book.setBookGenres(bookForm.getGenre().stream()
				.map(Genre::createGenre)
				.collect(Collectors.toSet())
			);
			book.setAuthor(bookForm.getAuthor());
			book.setISBN(bookForm.getISBN());
			book.setPublisher(bookForm.getPublisher());

			setStockEdit(item, item.getQuantity(), bookForm.getStock());
			shopProductInventory.save(item);
		});
		return "redirect:/inventory_book";

	}

	@PostMapping("/inventory/save_calendar/{itemId}")
	public String saveCalendar(@Valid AddMerchCalendarForm calendarForm, BindingResult result,
							   @RequestParam("imageFileCalendar") MultipartFile file,
							   @PathVariable Product.ProductIdentifier itemId, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}

		// this should update the path of the image if a new image is uploaded
		// otherwise keep the old path
		String imagePath;
		if (!file.isEmpty()) {
			imagePath = saveImage(file);
		} else {
			imagePath = calendarForm.getImage();
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Calendar calendar = (Calendar) item.getProduct();
			calendar.setName(calendarForm.getName());
			calendar.setImage(imagePath);
			calendar.setDescription(calendarForm.getDescription());
			calendar.setPrice(Money.of(calendarForm.getPrice(), "EUR"));

			setStockEdit(item, item.getQuantity(), calendarForm.getStock());

			shopProductInventory.save(item);
		});
		return "redirect:/inventory_calendar";

	}

	@PostMapping("/inventory/save_merch/{itemId}")
	public String saveMerch(@Valid AddMerchCalendarForm merchForm, BindingResult result,
							@RequestParam("imageFileMerch") MultipartFile file,
							@PathVariable Product.ProductIdentifier itemId, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}

		// this should update the path of the image if a new image is uploaded
		// otherwise keep the old path
		String imagePath;
		if (!file.isEmpty()) {
			imagePath = saveImage(file);
		} else {
			imagePath = merchForm.getImage();
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Merch merch = (Merch) item.getProduct();
			merch.setName(merchForm.getName());
			merch.setImage(imagePath);
			merch.setDescription(merchForm.getDescription());
			merch.setPrice(Money.of(merchForm.getPrice(), "EUR"));

			setStockEdit(item, item.getQuantity(), merchForm.getStock());
			shopProductInventory.save(item);
		});

		return "redirect:/inventory_merch";
	}


	@GetMapping("/inventory_calendar/out_of_stock")
	public String showOutOfStockCalendars(Model model) {
		List<Map.Entry<Calendar, Quantity>> outOfStockCalendars = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Calendar && (item.getQuantity().getAmount().compareTo(BigDecimal.ZERO) == 0)){
				outOfStockCalendars.add(new AbstractMap.SimpleEntry<>((Calendar) item.getProduct(), item.getQuantity()));
			}
		}

		if (outOfStockCalendars.isEmpty()) {
			return "redirect:/inventory_calendar";
		}
		// Add filtered calendars to the model
		model.addAttribute("calendars", outOfStockCalendars);
		model.addAttribute("viewName", "inventory_calendar");

		// Ensure form data is present to avoid errors on page reload
		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}

		return "inventory_calendar"; // Reuse the same template to display the out-of-stock items
	}

	@GetMapping("/inventory_book/out_of_stock")
	public String showOutOfStockBooks(Model model) {
		List<Map.Entry<Book, Quantity>> outOfStockBooks = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Book && item.getQuantity().getAmount().compareTo(BigDecimal.ZERO) == 0) {
				outOfStockBooks.add(new AbstractMap.SimpleEntry<>((Book) item.getProduct(), item.getQuantity()));
			}
		}

		if (outOfStockBooks.isEmpty()) {
			return "redirect:/inventory_book";
		}

		model.addAttribute("books", outOfStockBooks);
		model.addAttribute("viewName", "inventory_book");
		model.addAttribute("bookGenres_addBook", Genre.getAllGenres());

		if (!model.containsAttribute("bookForm")) {
			model.addAttribute("bookForm", new AddBookForm());
		}

		return "inventory_book";
	}
	@GetMapping("/inventory_merch/out_of_stock")
	public String showOutOfStockMerch(Model model) {
		// Filter out-of-stock merch
		List<Map.Entry<Merch, Quantity>> outOfStockMerch = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Merch && item.getQuantity().getAmount().compareTo(BigDecimal.ZERO) == 0) {
				outOfStockMerch.add(new AbstractMap.SimpleEntry<>((Merch) item.getProduct(), item.getQuantity()));
			}
		}

		if (outOfStockMerch.isEmpty()) {
			return "redirect:/inventory_merch";
		}

		model.addAttribute("merch", outOfStockMerch);
		model.addAttribute("viewName", "inventory_merch");

		if (!model.containsAttribute("merchForm")) {
			model.addAttribute("merchForm", new AddMerchCalendarForm());
		}

		return "inventory_merch";
	}

	@GetMapping("/inventory_book/sort_price_asc")
	public String sortBooksByPriceAsc(Model model) {

		List<Map.Entry<Book, Quantity>> books = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Book) {
				books.add(new AbstractMap.SimpleEntry<>((Book) item.getProduct(), item.getQuantity()));
			}
		}

		books.sort(Comparator.comparing(entry -> entry.getKey().getPrice())); // Sort the list in ascending order of price

		model.addAttribute("books", books); // Add sorted books to the model
		model.addAttribute("viewName", "inventory_book");

		model.addAttribute("bookGenres_addBook", Genre.getAllGenres());

		if (!model.containsAttribute("bookForm")) {
			model.addAttribute("bookForm", new AddBookForm());
		}

		return "inventory_book";
	}

	@GetMapping("/inventory_merch/sort_price_asc")
	public String sortMerchByPriceAsc(Model model) {
		// Fetch all merch into a normal list
		List<Map.Entry<Merch, Quantity>> merch = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Merch) {
				merch.add(new AbstractMap.SimpleEntry<>((Merch) item.getProduct(), item.getQuantity()));
			}
		}

		merch.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));

		model.addAttribute("merch", merch);
		model.addAttribute("viewName", "inventory_merch");

		if (!model.containsAttribute("merchForm")) {
			model.addAttribute("merchForm", new AddMerchCalendarForm());
		}

		return "inventory_merch";
	}

	@GetMapping("/inventory_calendar/sort_price_asc")
	public String sortCalendarsByPriceAsc(Model model) {

		List<Map.Entry<Calendar, Quantity>> calendars = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Calendar) {
				calendars.add(new AbstractMap.SimpleEntry<>((Calendar) item.getProduct(), item.getQuantity()));
			}
		}

		calendars.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));

		model.addAttribute("calendars", calendars);
		model.addAttribute("viewName", "inventory_calendar");

		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}
		return "inventory_calendar";
	}

	@GetMapping("/inventory_calendar/sort_alpha")
	public String sortCalendarsByName(Model model) {

		List<Map.Entry<Calendar, Quantity>> calendars = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Calendar) {
				calendars.add(new AbstractMap.SimpleEntry<>((Calendar) item.getProduct(), item.getQuantity()));
			}
		}

		calendars.sort(Comparator.comparing(entry -> entry.getKey().getName()));

		model.addAttribute("calendars", calendars);
		model.addAttribute("viewName", "inventory_calendar");

		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}

		return "inventory_calendar";
	}

	@GetMapping("/inventory_book/sort_alpha")
	public String sortBooksByName(Model model) {

		List<Map.Entry<Book, Quantity>> books = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Book) {
				books.add(new AbstractMap.SimpleEntry<>((Book) item.getProduct(), item.getQuantity()));
			}
		}
		books.sort(Comparator.comparing(entry -> entry.getKey().getName()));

		model.addAttribute("books", books);
		model.addAttribute("viewName", "inventory_book");

		model.addAttribute("bookGenres_addBook", Genre.getAllGenres());

		if (!model.containsAttribute("bookForm")) {
			model.addAttribute("bookForm", new AddBookForm());
		}

		return "inventory_book";
	}

	@GetMapping("/inventory_merch/sort_alpha")
	public String sortMerchByName(Model model) {

		List<Map.Entry<Merch, Quantity>> merch = new ArrayList<>();
		for (UniqueInventoryItem item : shopProductInventory.findAll()) {
			if (item.getProduct() instanceof Merch) {
				merch.add(new AbstractMap.SimpleEntry<>((Merch) item.getProduct(), item.getQuantity()));
			}
		}

		merch.sort(Comparator.comparing(entry -> entry.getKey().getName()));

		model.addAttribute("merch", merch);
		model.addAttribute("viewName", "inventory_merch");

		if (!model.containsAttribute("merchForm")) {
			model.addAttribute("merchForm", new AddMerchCalendarForm());
		}

		return "inventory_merch";
	}
}



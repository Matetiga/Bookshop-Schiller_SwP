package kickstart.Inventory;

import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

		}catch (IllegalArgumentException e) {
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
		for(UniqueInventoryItem item : shopProductInventory.findAll()){
			if(item.getProduct() instanceof Book){
				books.add(new AbstractMap.SimpleEntry<Book, Quantity>((Book) item.getProduct(), item.getQuantity()));
			}
		}
		// this prevents the form from being overwritten when the page is reloaded
		//TODO: this is not the most optimal way to do
		// the form is passed to the html only as a parameter and not a direct initialization
		if (!model.containsAttribute("bookForm")) {
			model.addAttribute("bookForm", new AddBookForm());
		}
		model.addAttribute("bookGenres_addBook", Genre.getAllGenres());
		model.addAttribute("books", books);
		return "inventory_book";
	}

	@GetMapping("/inventory_merch")
	public String showMerchInventory(Model model) {
		List<Map.Entry<Merch, Quantity>> merch = new ArrayList<>();
		for(UniqueInventoryItem item : shopProductInventory.findAll()){
			if(item.getProduct() instanceof Merch){
				merch.add(new AbstractMap.SimpleEntry<Merch, Quantity>((Merch) item.getProduct(), item.getQuantity()));
			}
		}

		if (!model.containsAttribute("merchForm")) {
			model.addAttribute("merchForm", new AddMerchCalendarForm());
		}

		model.addAttribute("merch", merch);
		return "inventory_merch";
	}

	@GetMapping("/inventory_calendar")
	public String showCalendarInventory(Model model) {
		List<Map.Entry<Calendar, Quantity>> calendars = new ArrayList<>();
		for(UniqueInventoryItem item : shopProductInventory.findAll()){
			if(item.getProduct() instanceof Calendar){
				calendars.add(new AbstractMap.SimpleEntry<Calendar, Quantity>((Calendar) item.getProduct(), item.getQuantity()));
			}
		}

		if (!model.containsAttribute("calendarForm")) {
			model.addAttribute("calendarForm", new AddMerchCalendarForm());
		}

		model.addAttribute("calendars", calendars);
		return "inventory_calendar";
	}


	@PostMapping("/inventory/increase")
	public String increaseProductQuantity(@RequestParam("itemId") Product.ProductIdentifier id ,
										  @RequestParam("viewName") String viewName,  Model model){
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();
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
	public String decreaseProductQuantity(@RequestParam("itemId") Product.ProductIdentifier id , Model model){
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();
		if (shopProduct.getProduct() instanceof Book) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				item.increaseQuantity(Quantity.of(-1));
				shopProductInventory.save(item);
			});
			showInventory(model);
			return "inventory_book";
		}
		if (shopProduct.getProduct() instanceof Calendar) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				item.increaseQuantity(Quantity.of(-1));
				shopProductInventory.save(item);

			});
			showCalendarInventory(model);
			return "redirect:/inventory_calendar";
		}
		if (shopProduct.getProduct() instanceof Merch) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				item.increaseQuantity(Quantity.of(-1));
				shopProductInventory.save(item);

			});
			showMerchInventory(model);
			return "redirect:/inventory_merch";
		}
		else{
			return "inventory_book";
		}

	}


	@PostMapping("/inventory/delete")
	public String deleteProduct(@RequestParam("itemId") Product.ProductIdentifier id, Model model){
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();
		if (shopProduct.getProduct() instanceof Book) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				shopProductInventory.delete(item);
			});
			showInventory(model);
			return "inventory_book";
		}
		if (shopProduct.getProduct() instanceof Calendar) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				shopProductInventory.delete(item);
			});
			showCalendarInventory(model);
			return "redirect:/inventory_calendar";
		}
		if (shopProduct.getProduct() instanceof Merch) {
			shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
				shopProductInventory.delete(item);
			});
			showMerchInventory(model);
			return "redirect:/inventory_merch";
		}
		else{
			return "inventory_book";
		}
	}

//	@GetMapping("/inventory/add_bookForm")
//	public String showAddBookForm(Model model, AddBookForm bookForm) {
//		model.addAttribute("bookForm", new AddBookForm());
//		return "inventory_book";
//	}


	@PostMapping("/inventory/add_book")
	public String addBook(@Valid AddBookForm bookForm,  BindingResult result, Model model){

		if(result.hasErrors()){
			showInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_book";
		}

		Book book = new Book(bookForm.getName(),
			bookForm.getImage(),
			Money.of(bookForm.getPrice(), "EUR"),
			bookForm.getDescription(),
			bookForm.getGenre().stream()
				.map(Genre::createGenre)
				.collect(Collectors.toSet()),
			bookForm.getAuthor(),
			bookForm.getISBN(),
			bookForm.getPublisher());
		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem( book, Quantity.of(bookForm.getStock())));
		showInventory(model);

		return "inventory_book";
	}

	@PostMapping("/inventory/add_merch")
	public String addMerch(@Valid @ModelAttribute("merchForm") AddMerchCalendarForm merchForm, BindingResult result, Model model){
		if(result.hasErrors()){
			showMerchInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_merch";
		}
		Merch merch = new Merch(merchForm.getName(),
			merchForm.getImage(),
			Money.of(merchForm.getPrice(), "EUR"),
			merchForm.getDescription());
		shopProductCatalog.save(merch);
		shopProductInventory.save(new UniqueInventoryItem( merch, Quantity.of(merchForm.getStock())));
		showMerchInventory(model);
		return "inventory_merch";
	}

	@PostMapping("/inventory/add_calendar")
	public String addCalendar(@Valid @ModelAttribute("calendarForm") AddMerchCalendarForm calendarForm, BindingResult result, Model model){
		if(result.hasErrors()){
			showCalendarInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_calendar";
		}
		Calendar calendar = new Calendar(calendarForm.getName(),
			calendarForm.getImage(),
			Money.of(calendarForm.getPrice(), "EUR"),
			calendarForm.getDescription());

		shopProductCatalog.save(calendar);
		shopProductInventory.save(new UniqueInventoryItem( calendar, Quantity.of(calendarForm.getStock())));
		showCalendarInventory(model);
		return "inventory_calendar";
	}



	@GetMapping("/inventory/editable/{itemId}")
	public String getDetail(@PathVariable Product.ProductIdentifier itemId, Model model) {
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(itemId).get();
		if (shopProduct.getProduct() instanceof Book) {
			model.addAttribute("bookID", shopProduct.getProduct().getId() );
			model.addAttribute("bookGenres", Genre.getAllGenres());
			// TODO: this may not be the most optimal way
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
			model.addAttribute("calendar", shopProduct.getProduct());
		}
		if (shopProduct.getProduct() instanceof Merch) {
			model.addAttribute("merch", shopProduct.getProduct());
		}

		return "inventory_editable";
	}


	@PostMapping("/inventory/save_book/{itemId}")
	public String saveBook(@Valid AddBookForm bookForm, BindingResult result, @PathVariable Product.ProductIdentifier itemId, Model model){
		if(result.hasErrors()){
			model.addAttribute("error_editing", "there was an error");
			model.addAttribute("bookForm", bookForm);
			getDetail(itemId, model);
			return "inventory_editable";
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			item.getProduct().setName(bookForm.getName());

			Money moneyPrice = Money.of(bookForm.getPrice(), "EUR");
			item.getProduct().setPrice(moneyPrice);

			((Book) item.getProduct()).setDescription(bookForm.getDescription());

			((Book) item.getProduct()).setAuthor(bookForm.getAuthor());
			((Book) item.getProduct()).setISBN(bookForm.getISBN());
			((Book) item.getProduct()).setPublisher(bookForm.getPublisher());
			((Book) item.getProduct()).setBookGenres(bookForm.getGenre().stream()
				.map(Genre::createGenre)
				.collect(Collectors.toSet())
			);

//			Book book = (Book) item.getProduct();
//
//			book.setName(bookForm.getName());
//			book.setImage(bookForm.getImage());
//			book.setPrice(Money.of(bookForm.getPrice(), "EUR"));
//			book.setDescription(bookForm.getDescription());
//			book.setBookGenres(bookForm.getGenre().stream()
//				.map(Genre::createGenre)
//				.collect(Collectors.toSet())
//			);
//			book.setAuthor(bookForm.getAuthor());
//			book.setISBN(bookForm.getISBN());
//			book.setPublisher(bookForm.getPublisher());

			shopProductInventory.save(item);
		});
		return "redirect:/inventory_book";

	}

	@PostMapping("/inventory/save_calendar")
	public String saveCalendar(
		@RequestParam("itemId") Product.ProductIdentifier id,
		@RequestParam("name") String name,
		@RequestParam("description") String desc,
		@RequestParam("price") BigDecimal price) {
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.getProduct().setName(name);

			Money moneyPrice = Money.of(price, "EUR");
			item.getProduct().setPrice(moneyPrice);

			((Calendar) item.getProduct()).setDescription(desc);

			shopProductInventory.save(item);
		});
		return "redirect:/inventory_calendar";

	}

	@PostMapping("/inventory/save_merch")
	public String saveMerch(
		@RequestParam("itemId") Product.ProductIdentifier id,
		@RequestParam("name") String name,
		@RequestParam("description") String desc,
		@RequestParam("price") BigDecimal price) {
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.getProduct().setName(name);

			Money moneyPrice = Money.of(price, "EUR");
			item.getProduct().setPrice(moneyPrice);

			((Merch) item.getProduct()).setDescription(desc);

			shopProductInventory.save(item);
		});

		return "redirect:/inventory_merch";
	}


	@GetMapping("/inventory_testing")
	public String testingForm(Model model, TestingForm form){
		return "inventory_testing";
	}

	@PostMapping("/inventory_testing")
	public String testingForm(@Valid TestingForm form, BindingResult result ,Model model){
		if(result.hasErrors()){
			return "inventory_testing";

		}

		model.addAttribute("passed", "Form has been passed");
		return "inventory_testing";
	}
}



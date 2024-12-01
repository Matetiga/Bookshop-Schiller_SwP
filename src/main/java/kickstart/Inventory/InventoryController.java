package kickstart.Inventory;

import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
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

@Controller
public class InventoryController {
	@ManyToOne
	private final UniqueInventory<UniqueInventoryItem> shopProductInventory;
	private final ShopProductCatalog shopProductCatalog;


	InventoryController(UniqueInventory<UniqueInventoryItem> shopProductInventory, ShopProductCatalog shopProductCatalog) {
		this.shopProductInventory = shopProductInventory;
		this.shopProductCatalog = shopProductCatalog;
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
//		model.addAttribute("bookForm", bookForm);
//		return "inventory_book";
//	}


	@PostMapping("/inventory/add_book")
	public String addBook(@Valid @ModelAttribute("bookForm") AddBookForm bookForm,  BindingResult result, Model model){

		if(result.hasErrors()){
			showInventory(model);
			model.addAttribute("showModal", true);
			return "inventory_book";
		}


		Book book = new Book(bookForm.getName(),
			bookForm.getImage(),
			Money.of(bookForm.getPrice(), "EUR"),
			bookForm.getDescription(),
			Genre.createGenre(bookForm.getGenre()),
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



	@PostMapping("/inventory/editable")
	public String getDetail(@RequestParam("itemId") Product.ProductIdentifier id, Model model) {
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(id).get();
		if (shopProduct.getProduct() instanceof Book) {
			model.addAttribute("book", shopProduct.getProduct() );
			model.addAttribute("bookGenres", Genre.getAllGenres());
		}
		if (shopProduct.getProduct() instanceof Calendar) {
			model.addAttribute("calendar", shopProduct.getProduct());
		}
		if (shopProduct.getProduct() instanceof Merch) {
			model.addAttribute("merch", shopProduct.getProduct());
		}

		return "inventory_editable";
	}

	//TODO: Invalid arguments are allowed to be passed
	// check for empty Strings or negative values
	@PostMapping("/inventory/save_book")
	public String saveBook(
		@RequestParam("itemId") Product.ProductIdentifier id,
		@RequestParam("name") String name,
		@RequestParam("description") String desc,
		@RequestParam("price") BigDecimal price,
		@RequestParam("genre")String genreName,
		@RequestParam("author")String author,
		@RequestParam("ISBN") String isbn,
		@RequestParam("publisher")String publisher) {
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.getProduct().setName(name);

			Money moneyPrice = Money.of(price, "EUR");
			item.getProduct().setPrice(moneyPrice);

			((Book) item.getProduct()).setDescription(desc);

			((Book) item.getProduct()).setAuthor(author);
			((Book) item.getProduct()).setISBN(isbn);
			((Book) item.getProduct()).setPublisher(publisher);

			Genre genre = Genre.createGenre(genreName);
			((Book) item.getProduct()).setGenre(genre);

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

}



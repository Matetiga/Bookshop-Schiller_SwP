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
		// this prevents the form from being overwritten when the page is re rendered
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


	// This method should give all the information of the different products to the html
	// depending on its class
	@GetMapping("/inventory/editable/{itemId}")
	public String getDetail(@PathVariable Product.ProductIdentifier itemId, Model model) {
		UniqueInventoryItem shopProduct = shopProductInventory.findByProductIdentifier(itemId).get();
		model.addAttribute("productId", shopProduct.getProduct().getId() );
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

	private void setStockEdit(UniqueInventoryItem item , Quantity oldQuantity, int newQuantity){
		int oldStock = oldQuantity.getAmount().intValue();

		if(oldStock < newQuantity){
			item.increaseQuantity(Quantity.of(newQuantity - oldStock));
		}
		if(oldStock > newQuantity){
			item.decreaseQuantity(Quantity.of(oldStock - newQuantity));
		}
	}


	@PostMapping("/inventory/save_book/{itemId}")
	public String saveBook(@Valid AddBookForm bookForm, BindingResult result,
						   @PathVariable Product.ProductIdentifier itemId, Model model){

		if(result.hasErrors()){
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Book book = (Book) item.getProduct();
			book.setName(bookForm.getName());
			book.setImage(bookForm.getImage());
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
							   @PathVariable Product.ProductIdentifier itemId, Model model) {
		if(result.hasErrors()){
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Calendar calendar = (Calendar) item.getProduct();
			calendar.setName(calendarForm.getName());
			calendar.setImage(calendarForm.getImage());
			calendar.setDescription(calendarForm.getDescription());
			calendar.setPrice(Money.of(calendarForm.getPrice(), "EUR"));

			setStockEdit(item, item.getQuantity(), calendarForm.getStock());

			shopProductInventory.save(item);
		});
		return "redirect:/inventory_calendar";

	}

	@PostMapping("/inventory/save_merch/{itemId}")
	public String saveMerch(@Valid AddMerchCalendarForm merchForm, BindingResult result,
							@PathVariable Product.ProductIdentifier itemId, Model model) {
		if(result.hasErrors()){
			model.addAttribute("fieldErrors", result.getFieldErrors());
			getDetail(itemId, model);
			return "inventory_editable";
		}

		shopProductInventory.findByProductIdentifier(itemId).ifPresent(item -> {
			Merch merch = (Merch) item.getProduct();
			merch.setName(merchForm.getName());
			merch.setImage(merchForm.getImage());
			merch.setDescription(merchForm.getDescription());
			merch.setPrice(Money.of(merchForm.getPrice(), "EUR"));

			setStockEdit(item, item.getQuantity(), merchForm.getStock());
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



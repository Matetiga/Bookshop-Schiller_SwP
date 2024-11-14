package kickstart.Inventory;

import kickstart.Inventory.Products.Book;
import kickstart.Inventory.Products.Calendar;
import kickstart.Inventory.Products.Merch;
import kickstart.Inventory.Products.ShopProduct;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class InventoryController {

	private final UniqueInventory<UniqueInventoryItem> bookInventory;
	private final UniqueInventory<UniqueInventoryItem> calendarInventory;
	private final UniqueInventory<UniqueInventoryItem> merchInventory;
	private final BookCatalog bookCatalog;

	InventoryController(UniqueInventory<UniqueInventoryItem> bookInventory, UniqueInventory<UniqueInventoryItem> calendarInventory,
						UniqueInventory<UniqueInventoryItem> merchInventory, BookCatalog bookCatalog) {

		this.calendarInventory = calendarInventory;
		this.merchInventory = merchInventory;
		this.bookInventory = bookInventory;
		this.bookCatalog = bookCatalog;
	}

	@GetMapping("/")
	public String showWelcomePage() {
		return "basicWelcome"; // this refers to the 'welcome' Thymeleaf template
	}

	// testing
	@GetMapping("/inventory")
	public String showInventory(Model model) {
		model.addAttribute("books", bookInventory.findAll());
		model.addAttribute("calendars", calendarInventory.findAll());
		Set<String> test = Set.of("test1", "test2", "test3");
		model.addAttribute("test", test);
		model.addAttribute("merch", merchInventory.findAll());
		return "inventory";
		bookInventory.findByProduct().increaseQuantity(1);
	}

	@GetMapping("/inventory/merch")
	public String showMerchInventory(){
		merchInventory.findAll();
		return "merchInventory";
	}

	@GetMapping("/inventory/calendars")
	public String showCalInventory(){
		calendarInventory.findAll();
		return "calInventory";
	}

	@PostMapping("/inventory")
	public String increaseQuant(){
		bookInventory.findByProduct().
	}
}



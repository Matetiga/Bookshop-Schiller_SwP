package kickstart.Inventory;

import jakarta.persistence.ManyToOne;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

		model.addAttribute("calendars", calendars);
		return "inventory_calendar";
	}

	//TODO ASK IF THERE IS A BETTER WAY TO DO THIS
	// should a class like this be created for every inventory item? (merch_inventory, calendar_inventory)
	// now it redirects to the main inventory page
	// can this class be used for the three with a parameter?
	@PostMapping("/inventory/update")
	public String updateProductQuantity(@RequestParam("itemId") Product.ProductIdentifier id , Model model){
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.increaseQuantity(Quantity.of(1));
			shopProductInventory.save(item);
		});
		showInventory(model);
		return "inventory_book";

	}

	@PostMapping("/inventory/edit")
	public String editProductName(@RequestParam("itemId") Product.ProductIdentifier id, @RequestParam("newName") String newName, Model model){
		if (newName.isBlank()){
			model.addAttribute("edit_error", "Error while editing a book name");
			showInventory(model);
			return "inventory_book";
		}
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			item.getProduct().setName(newName);
			shopProductInventory.save(item);
		});

		showInventory(model);
		return "inventory_book";
	}

	@PostMapping("/inventory/delete")
	public String deleteProduct(@RequestParam("itemId") Product.ProductIdentifier id, Model model){
		shopProductInventory.findByProductIdentifier(id).ifPresent(item -> {
			shopProductInventory.delete(item);
		});
		showInventory(model);
		return "inventory_book";
	}

	@PostMapping("/inventory/add_book")
	public String addProduct(@RequestParam("name") String name,@RequestParam("stock") int stock, Model model){
//		@RequestParam("name") String name, @RequestParam("stock") int stock,
//							 @RequestParam("image") String image, @RequestParam("price") double price,
//							 @RequestParam("description") String description, @RequestParam("genre") String genre,
//							 @RequestParam("author") String author, @RequestParam("ISBN") String ISBN,
//							 @RequestParam("publisher") String publisher, Model model, Errors error){
		if (name.isBlank()){
			model.addAttribute("blankName_error", "Name cannot be blank");
			model.addAttribute("showModal", true);
			return "inventory_book";
		}
		if (stock <= 0){
			model.addAttribute("negativeStock_error", "Stock cannot be negative");
			return "inventory_book";
		}




		Book book = new Book(name, "stephen", Money.of(10.99, "EUR"),
				"A novel set in the 1920s about the American Dream", Genre.createGenre("Fiction"), "F. Scott Fitzgerald",
				"9780743273565", "Scribner");
		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem( book, Quantity.of(stock)));
		showInventory(model);
		return "inventory_book";
	}

	@PostMapping("/inventory/editable")
	public String getDetail(@RequestParam("itemId") Product.ProductIdentifier id, Model model ){
		UniqueInventoryItem ShopProduct = shopProductInventory.findByProductIdentifier(id).get();
		if(ShopProduct.getProduct() instanceof  Book){
			model.addAttribute("book", model);
		}
		if(ShopProduct.getProduct() instanceof Calendar){
			model.addAttribute("calendar", model);
		}
		if(ShopProduct.getProduct() instanceof Merch){
			model.addAttribute("merch", model);
		}
		return "inventory_editable";
	}



}



package kickstart.Inventory;

import kickstart.Inventory.Products.Book;
import kickstart.Inventory.Products.Calendar;
import kickstart.Inventory.Products.Merch;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class InventoryController {
	private final UniqueInventory<UniqueInventoryItem> shopProductInventory;

	InventoryController(UniqueInventory<UniqueInventoryItem> shopProductInventory) {
		this.shopProductInventory = shopProductInventory;
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

}



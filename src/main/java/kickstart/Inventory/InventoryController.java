package kickstart.Inventory;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

	private final UniqueInventory<UniqueInventoryItem> inventory;

	InventoryController(UniqueInventory<UniqueInventoryItem> inventory) {
		this.inventory = inventory;
	}

	@GetMapping("/")
	public String showWelcomePage() {
		return "basicWelcome"; // this refers to the 'welcome' Thymeleaf template
	}

	@GetMapping("/inventory")
	public String showInventory(Model model) {
		model.addAttribute("products", inventory.findAll());
		return "inventory";
	}
}

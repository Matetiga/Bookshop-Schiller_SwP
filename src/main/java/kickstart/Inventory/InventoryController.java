package kickstart.Inventory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {


	@GetMapping("/")
	public String showWelcomePage() {
		return "basicWelcome"; // this refers to the 'welcome' Thymeleaf template
	}
	@GetMapping("/inventory")
	public String inventoryTable(){
		return "inventory";
	}
}

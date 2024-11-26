package kickstart.orders;

import jakarta.servlet.http.HttpServletRequest;
import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static kickstart.Inventory.Genre.createGenre;


@Controller
@SessionAttributes("cart")
public class OrderController {
	private UserAccount.UserAccountIdentifier userId;
	private final MyOrderRepository myOrderRepository;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final UserManagement userManagement;

	Genre fiction = createGenre("Fiction");
	Genre history = createGenre("Cooking");
	//for testing:
	private final Product exampleProduct1 = new Book("The Great Gatsby", "gatsby.jpg", Money.of(10 ,"EUR"),
		"A novel set in the 1920s about the American Dream", fiction, "F. Scott Fitzgerald",
		"9780743273565", "Scribner");
	private final Product exampleProduct2 = new Book("Sapiens: A Brief History of Humankind", "sapiens.jpg", Money.of(15, "EUR"),
		"Explores the history of humankind", history, "Yuval Noah Harari",
		"9780062316110", "Harper");

	OrderController(MyOrderRepository myOrderRepository, UniqueInventory<UniqueInventoryItem> inventory, UserManagement userManagement){
		this.myOrderRepository = myOrderRepository;
		this.userManagement = userManagement;
		this.userId = UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString());
		this.inventory = inventory;
	}

	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	@GetMapping("/cart")
	String cart() {
		return "cart";
	}

	@PostMapping("/cartAdd")
	String cartAdd(@ModelAttribute Cart cart, @RequestParam("productId") Product.ProductIdentifier productId, Model model, HttpServletRequest request){
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), 1);
		model.addAttribute("message", "Produkt wurde erfolgreich zum Warenkorb hinzugefügt!");

		String referer = request.getHeader("Referer");

		return "redirect:" + referer;
	}
	//Test-Method for adding products to the cart
	//mapped to the temporary buttons in cart.html
	@PostMapping("/cartAddExample1")
	String addProductExample1(@ModelAttribute Cart cart) {
		cart.addOrUpdateItem(exampleProduct1, 1);
		return "cart";
	}

	//Test-Method for adding products to the cart
	//mapped to the temporary buttons in cart.html
	@PostMapping("/cartAddExample2")
	String addProductExample2(@ModelAttribute Cart cart) {
		cart.addOrUpdateItem(exampleProduct2, 1);
		return "cart";
	}

	@PostMapping("/cartDelete")
	String deleteProduct(@RequestParam("productId") String productId, @ModelAttribute Cart cart) {
		cart.removeItem(productId);
		return "cart";
	}

	@PostMapping("/clear")
	String clearCart(@ModelAttribute Cart cart) {
		cart.clear();
		return "cart";
	}

	@PostMapping("/checkout")
	String buy(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Cart cart, @RequestParam("paymentMethod") String paymentMethod) {
		MyOrder order = null;
		for (User user : userManagement.findAll()) {
			if (user.getUserAccount().getUsername().equals(userDetails.getUsername())) {
				order = new MyOrder(user.getUserAccount().getId(), paymentMethod);

			}
		}
		if(order != null){
			for(CartItem item : cart.stream().toList()){
				order.addOrderLine(item.getProduct(), item.getQuantity());
			}
			deleteProductsFromStock(order);
			myOrderRepository.save(order);
			cart.clear();
		}
		return "cart";
	}

	private void deleteProductsFromStock(MyOrder order){
		for(OrderLine orderLine : order.getOrderLines()){
			//orderLine.getProductName()
			//orderLine.getId()
			//orderLine.getQuantity()
		}
	}


}

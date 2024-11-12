package kickstart.orders;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@SessionAttributes("cart")
public class OrderController {
	private UserAccount.UserAccountIdentifier userId;

	private final OrderViewController orderViewController;

	OrderController(OrderViewController orderViewController){
		this.orderViewController = orderViewController;
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
	String addProduct(@ModelAttribute Cart cart) {
		//@RequestParam("product") Product product,
		Product product = new Book("geiles Buch", Money.of(100, "EUR"), Book.Genre.Fantasy, "Jesus");
		Product product2 = new Book("schlechtes Buch", Money.of(200, "EUR"), Book.Genre.Fantasy, "Arno Dübel");
		cart.addOrUpdateItem(product, 1);
		cart.addOrUpdateItem(product2, 3);
		return "cart";
	}

	@PostMapping("/cartDelete")
	String deleteProduct(@RequestParam("product") Product product, @ModelAttribute Cart cart) {
		return "cart";
	}

	@PostMapping("/clear")
	String clearCart(@ModelAttribute Cart cart) {
		cart.clear();
		return "cart";
	}

	@PostMapping("/checkout")
	String checkOut(@ModelAttribute Cart cart) {
		Order order = new Order(userId);
		for(CartItem item : cart.stream().toList()){
			order.addOrderLine(item.getProduct(), item.getQuantity());
		}

		return "cart";
	}
}

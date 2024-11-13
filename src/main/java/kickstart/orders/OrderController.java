package kickstart.orders;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@SessionAttributes("cart")
public class OrderController {
	private UserAccount.UserAccountIdentifier userId;
	private final MyOrderRepository myOrderRepository;

	//for testing:
	private static final Product exampleProduct1 = new Book("geiles Buch", Money.of(100, "EUR"), Book.Genre.Fantasy, "Jesus");
	private final Product exampleProduct2 = new Book("schlechtes Buch", Money.of(200, "EUR"), Book.Genre.Fantasy, "Arno Dübel");

	OrderController(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
		this.userId = UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString());
	}

	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	@GetMapping("/cart")
	String cart() {
		return "cart";
	}

	//actual Method for adding products to the cart
	//"/cartAdd" isn't currently used in html for the purpose of demonstration, instead "/cardAddExample1/2"
	@PostMapping("/cartAdd")
	String addProduct(@ModelAttribute Cart cart, @RequestParam("product")Product product, @RequestParam("amount") long amount) {
		cart.addOrUpdateItem(product, amount);
		return "cart";
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
	String checkOut(@ModelAttribute Cart cart, @RequestParam("paymentMethod") String paymentMethod) {
		MyOrder order = new MyOrder(userId, paymentMethod);
		for(CartItem item : cart.stream().toList()){
			order.addOrderLine(item.getProduct(), item.getQuantity());
		}
		myOrderRepository.save(order);
		cart.clear();
		return "cart";
	}

	@PostMapping("/welcome")
	String welcome(){
		return "welcome";
	}
}

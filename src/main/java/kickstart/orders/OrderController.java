package kickstart.orders;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;


@Controller
@SessionAttributes("cart")
public class OrderController {
	private UserAccount.UserAccountIdentifier userId;
	//private final OrderViewController orderViewController;
	private final MyOrderRepository myOrderRepository;
	//private PaymentMethod paymentMethod;

	OrderController(MyOrderRepository myOrderRepository){
		//this.orderViewController = orderViewController;
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
}

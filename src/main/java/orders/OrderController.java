package orders;

import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
	private final OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository){
		this.orderRepository = orderRepository;
	}

	@GetMapping("/cart")
	String cart() {
		return "cart";
	}

	@PostMapping("/cart")
	String addProduct(@RequestParam("product") Product product, @RequestParam("number") int number, @ModelAttribute Cart cart) {

		int amount = number <= 0 || number > 5 ? 1 : number;

		//cart.addOrUpdateItem(product, Quantity.of(amount));

		return "cart";
	}
}

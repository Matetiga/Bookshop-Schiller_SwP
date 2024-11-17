package kickstart.orders;

import kickstart.Inventory.Products.Book;
import kickstart.Inventory.Products.Genre;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static kickstart.Inventory.Products.Genre.createGenre;


@Controller
@SessionAttributes("cart")
public class OrderController {
	private UserAccount.UserAccountIdentifier userId;
	private final MyOrderRepository myOrderRepository;

	static Genre fiction = createGenre("Fiction");
	static Genre history = createGenre("Cooking");
	//for testing:
	private static final Product exampleProduct1 = new Book("The Great Gatsby", "gatsby.jpg", Money.of(10 ,"USD"),
		"A novel set in the 1920s about the American Dream", fiction, "F. Scott Fitzgerald",
		"9780743273565", "Scribner");
	private final Product exampleProduct2 = new Book("Sapiens: A Brief History of Humankind", "sapiens.jpg", Money.of(15, "USD"),
		"Explores the history of humankind", history, "Yuval Noah Harari",
		"9780062316110", "Harper");

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
	String buy(@ModelAttribute Cart cart, @RequestParam("paymentMethod") String paymentMethod) {
		MyOrder order = new MyOrder(userId, paymentMethod);
		for(CartItem item : cart.stream().toList()){
			order.addOrderLine(item.getProduct(), item.getQuantity());
		}
		myOrderRepository.save(order);
		cart.clear();
		return "cart";
	}
}

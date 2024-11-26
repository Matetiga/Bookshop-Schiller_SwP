package kickstart.orders;

import jakarta.servlet.http.HttpServletRequest;
import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.useraccount.UserAccount;
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


	OrderController(MyOrderRepository myOrderRepository, UniqueInventory<UniqueInventoryItem> inventory){
		this.myOrderRepository = myOrderRepository;
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
	String cartAdd(@ModelAttribute Cart cart, @RequestParam("productId") Product.ProductIdentifier productId, HttpServletRequest request){
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), 1);

		String referer = request.getHeader("Referer");

		return "redirect:" + referer;
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

	@PostMapping ("/cartIncrease")
	String increaseQuantity(@RequestParam("productId") Product.ProductIdentifier productId, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), 1);
		return "redirect:/cart";
	}

	@PostMapping ("/cartDecrease")
	String decreaseQuantity(@RequestParam("productId") Product.ProductIdentifier productId, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), -1);
		return "redirect:/cart";
	}
}

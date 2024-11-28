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
	String cartAdd(@ModelAttribute Cart cart, @RequestParam("productId") Product.ProductIdentifier productId, @RequestParam("amount") long amount, HttpServletRequest request){
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), amount);

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
	String buy(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Cart cart, @RequestParam("paymentMethod") String paymentMethod, Model model) {
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

			// if there are not enough items in stock
			try{
				deleteProductsFromStock(order);

			}catch(IllegalArgumentException e){
				System.out.println("error catched");
				model.addAttribute("error_NotEnoughStock", e.getMessage());
				return "cart";
			}

			myOrderRepository.save(order);
			cart.clear();
		}
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

	public void deleteProductsFromStock(MyOrder order){
		for(OrderLine orderLine : order.getOrderLines()){
			inventory.findByProductIdentifier(orderLine.getProductIdentifier()).ifPresent(item -> {
				if(item.getQuantity().isLessThan(orderLine.getQuantity())){
					throw new IllegalArgumentException("Not enough items in stock");
				}
				item.decreaseQuantity(orderLine.getQuantity());
				inventory.save(item);
			});
		}
	}


}

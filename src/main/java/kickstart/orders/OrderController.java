package kickstart.orders;

import jakarta.servlet.http.HttpServletRequest;
import kickstart.Achievement.Achievement;
import kickstart.Service.UserAchievementService;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("cart")
public class OrderController {
	private final MyOrderRepository myOrderRepository;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final UserManagement userManagement;
	private final MyOrderManagement myOrderManagement;
	private final UserAchievementService userAchievementService;

	OrderController(MyOrderRepository myOrderRepository, UniqueInventory<UniqueInventoryItem> inventory, UserManagement userManagement, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.userManagement = userManagement;
		this.inventory = inventory;
		this.myOrderManagement = myOrderManagement;
		this.userAchievementService = new UserAchievementService(this.userManagement);
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
	String cartAdd(@ModelAttribute Cart cart, @RequestParam("productId") Product.ProductIdentifier productId,
				   @RequestParam("amount") long amount, HttpServletRequest request){
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
	String buy(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Cart cart,
			   @RequestParam("paymentMethod") String paymentMethod, Model model) {
		MyOrder order = null;
		for (User user : userManagement.findAll()) {
			if (user.getUserAccount().getUsername().equals(userDetails.getUsername())) {
				order = new MyOrder(user, paymentMethod);
			}
		}
		if(order != null){
			for(CartItem item : cart.stream().toList()){
				order.addOrderLine(item.getProduct(), item.getQuantity());
			}

			// if there are not enough items in stock
			try{
				deleteProductsFromStock(order, model);

			}catch(IllegalArgumentException e){
				model.addAttribute("error_NotEnoughStock", true);
				return "cart";
			}

			// Achievement for buying all Snoop Dogg's copies
			cart.stream()
				.filter(item -> item.getProduct().getName().equals("From Crook To Cook: Platinum Recipes From Tha Boss Dogg's Kitchen"))
				.findFirst()
				.ifPresent(item -> {
					if(item.getQuantity().getAmount().intValue() == 420){
						if (!model.containsAttribute("achievement")) {

							Achievement achievement = new Achievement("Pass in the buffffffffffffffffffffffffff",
								"yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeah", Role.of("CUSTOMER"));
							userAchievementService.processAchievement(userDetails, achievement, model);

						}
					}
				});

			//model.addAttribute("error_NotEnoughStock", false);
			myOrderRepository.save(order);
			cart.clear();
		}
		myOrderManagement.setDeliveryState(myOrderRepository.findAll());
		model.addAttribute("order", order);

		return "my-order-details";
	}


	@PostMapping ("/cartIncrease")
	public String increaseQuantity(@RequestParam("productId") Product.ProductIdentifier productId,
								   @ModelAttribute Cart cart, Model model) {
		int quantity = cart.getQuantity(productId).getAmount().intValue();
		int stock = inventory.findByProductIdentifier(productId).get().getQuantity().getAmount().intValue();
		if (quantity == stock){
			model.addAttribute("error_NotEnoughStock", true);
			model.addAttribute("error_ArticleName", inventory.findByProductIdentifier(productId).get().getProduct().getName());
			return "cart";
		}
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), 1);
		return "redirect:/cart";
	}

	@PostMapping ("/cartDecrease")
	String decreaseQuantity(@RequestParam("productId") Product.ProductIdentifier productId, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(inventory.findByProductIdentifier(productId).get().getProduct(), -1);
		return "redirect:/cart";
	}

	public void deleteProductsFromStock(MyOrder order, Model model){
		for(OrderLine orderLine : order.getOrderLines()){
			inventory.findByProductIdentifier(orderLine.getProductIdentifier()).ifPresent(item -> {
				if(item.getQuantity().isLessThan(orderLine.getQuantity())){
					model.addAttribute("error_ArticleName", item.getProduct().getName());
					throw new IllegalArgumentException("Not enough items in stock");
				}else{
					item.decreaseQuantity(orderLine.getQuantity());
					inventory.save(item);
				}
			});
		}
	}
}

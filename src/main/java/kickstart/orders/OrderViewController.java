package kickstart.orders;

import jakarta.servlet.http.HttpServletRequest;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

@Controller
@SessionAttributes({"orderStates", "selectedState", "paymentMethods", "selectedPaymentMethod"})
public class OrderViewController {
	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;
	private final String[] orderStates = {"Alle", "Offen", "Abholbereit", "Abgeschlossen", "in Lieferung", "geliefert"};
	private final String[] paymentMethods = {"Alle", "Bar", "Rechnung"};

	//for initializing demo orders:
	private boolean isInitialized = false;

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
	}

	@ModelAttribute("orderStates")
	String[] initalizeOrderStates() {
		return this.orderStates;
	}

	@ModelAttribute("paymentMethods")
	String[] initalizePaymentMethods() {
		return this.paymentMethods;
	}

	@GetMapping("/order-overview")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	String orderOverview(Model model){

		if(!isInitialized){
			myOrderManagement.initializeRandomOrders();
			isInitialized = true;
		}

		myOrderManagement.setDeliveryState(myOrderRepository.findAll());

		model.addAttribute("orderList", myOrderRepository.findAll());
		model.addAttribute("selectedState", "Alle");
		model.addAttribute("selectedPaymentMethod", "Alle");

		return "order-overview";
	}

	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){

		myOrderRepository.deleteById(orderId);

		model.addAttribute("orderList", myOrderRepository.findAll());

		return "redirect:/order-overview";
	}

	@PostMapping("/changeStatus")
	String changeStatus(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		MyOrder order = myOrderRepository.findById(orderId).get();
		order.changeStatus();
		myOrderRepository.save(order);

		model.addAttribute("order", order);

		return "order-details";
	}

	@PostMapping("/filterOrders")
	String filterOrders(@RequestParam("filterState") String state, @RequestParam("filterPaymentMethod") String paymentMethod, @RequestParam(value = "productId", required = false, defaultValue = "empty%7") Product.ProductIdentifier productId, @RequestParam(value = "productName", required = false, defaultValue = "empty%7") String productName, @RequestParam(value = "userId", required = false, defaultValue = "empty%7") String userId, Model model){
		Iterable<MyOrder> filterList = myOrderManagement.findByStatus(state, myOrderRepository.findAll());
		filterList = myOrderManagement.findByPaymentMethod(paymentMethod, filterList);
		System.out.println(filterList);
		if (!productId.toString().equals("empty%7")){
			filterList = myOrderManagement.findByProductId(productId, filterList);
		}
		if (!productName.equals("empty%7")) {
			filterList = myOrderManagement.findByProductName(productName, filterList);
		}
		if (!userId.equals("empty%7")){
			filterList = myOrderManagement.findByUsername(userId, filterList);
		}
		System.out.println(filterList);
		model.addAttribute("orderList", filterList);
		model.addAttribute("selectedState", state);
		model.addAttribute("selectedPaymentMethod", paymentMethod);
		return "order-overview";
	}

	//currently not used
	@PostMapping("/sortByDate")
	String sortByDate(Model model){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		myOrderRepository.findAll().forEach(orderList::add);
		Collections.sort(orderList, Comparator.comparing(Order::getDateCreated));

		model.addAttribute("orderList", orderList);

		return "order-overview";
	}

	@PostMapping("/order/{orderID}")
	public String orderDetailsByIDAdmin(@PathVariable("orderID") Order.OrderIdentifier orderId, Model model) {
		myOrderManagement.setDeliveryState(myOrderRepository.findAll());
		MyOrder order = myOrderManagement.findByID(orderId);

		model.addAttribute("order", order);

		return "order-details";
	}

	@PostMapping("/my-order/{orderID}")
	public String orderDetailsByID(@PathVariable("orderID") Order.OrderIdentifier orderId, Model model) {
		myOrderManagement.setDeliveryState(myOrderRepository.findAll());
		MyOrder order = myOrderManagement.findByID(orderId);

		model.addAttribute("order", order);

		return "my-order-details";
	}

	@GetMapping("/order-details")
	String orderDetails(){
		return "order-details";
	}

	@GetMapping("/my-orders")
	@PreAuthorize("isAuthenticated()")
	String myOrders(Model model, @AuthenticationPrincipal UserDetails UserDetails){
		myOrderManagement.setDeliveryState(myOrderRepository.findAll());

		model.addAttribute("orderList", myOrderManagement.findByUsername(UserDetails.getUsername(), myOrderRepository.findAll()));

		return "my-orders";
	}

}

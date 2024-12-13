package kickstart.orders;

import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Controller
@SessionAttributes({"orderStates", "paymentMethods"})
public class OrderViewController {
	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;
	private final String[] orderStates = {"Alle", "Offen", "Abholbereit", "Abgeschlossen", "in Lieferung", "geliefert"};
	private final String[] paymentMethods = {"Alle", "Bar", "Rechnung"};

	private String[] lastFilterOptions = {"Alle", "Alle", "", ""};
	private String lastSortDateValue;
	private String lastSortDateValueKonto;

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
		lastSortDateValue = "neueste";
		lastSortDateValueKonto = "neueste";
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
		myOrderManagement.setDeliveryState(myOrderRepository.findAll());

		List<MyOrder> filteredList = (List<MyOrder>) myOrderManagement.filterAllOrders(lastFilterOptions[0], lastFilterOptions[1], lastFilterOptions[2], lastFilterOptions[3]);

		List<MyOrder> orderList = myOrderManagement.sortByDate(filteredList);
		if (lastSortDateValue.equals("neueste")) {
			orderList =  orderList.reversed();
		}

		model.addAttribute("orderList", orderList);
		model.addAttribute("selectedState", lastFilterOptions[0]);
		model.addAttribute("selectedPaymentMethod", lastFilterOptions[1]);
		model.addAttribute("selectedProduct", lastFilterOptions[2]);
		model.addAttribute("selectedUsername", lastFilterOptions[3]);
		model.addAttribute("sortDateButtonValue", lastSortDateValue);


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
	String filterOrders(@RequestParam("filterState") String state, @RequestParam("filterPaymentMethod") String paymentMethod, @RequestParam(value = "productName", required = false, defaultValue = "") String productName, @RequestParam(value = "userId", required = false, defaultValue = "") String username, Model model){
		Iterable<MyOrder> filteredList = myOrderManagement.filterAllOrders(state, paymentMethod, productName, username);

		List<MyOrder> orderList = myOrderManagement.sortByDate(filteredList);
		if (lastSortDateValue.equals("neueste")) {
			orderList = orderList.reversed();
		}

		model.addAttribute("orderList", orderList);
		model.addAttribute("selectedState", state);
		model.addAttribute("selectedPaymentMethod", paymentMethod);
		model.addAttribute("selectedProduct", productName);
		model.addAttribute("selectedUsername", username);
		model.addAttribute("sortDateButtonValue", lastSortDateValue);

		this.lastFilterOptions = new String[] {state, paymentMethod, productName, username};

		return "order-overview";
	}

	@PostMapping("/sortByDate")
	String sortByDate(Model model){
		List<MyOrder> orderList = myOrderManagement.sortByDate(myOrderManagement.filterAllOrders(lastFilterOptions[0], lastFilterOptions[1], lastFilterOptions[2], lastFilterOptions[3]));

		if(lastSortDateValue.equals("neueste")){
			lastSortDateValue = "älteste";
		}else {
			orderList = orderList.reversed();
			lastSortDateValue = "neueste";
		}

		model.addAttribute("orderList", orderList);
		model.addAttribute("selectedState", lastFilterOptions[0]);
		model.addAttribute("selectedPaymentMethod", lastFilterOptions[1]);
		model.addAttribute("selectedProduct", lastFilterOptions[2]);
		model.addAttribute("selectedUsername", lastFilterOptions[3]);
		model.addAttribute("sortDateButtonValue", lastSortDateValue);

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

		Iterable<MyOrder> orderList = myOrderManagement.findByUsername(UserDetails.getUsername(), myOrderRepository.findAll());

		model.addAttribute("orderList", myOrderManagement.sortByDate(orderList).reversed());
		model.addAttribute("sortDateButtonValueKonto", "neueste");

		return "my-orders";
	}

	@PostMapping("/sortByDateKonto")
	String sortDateKontoOrders(Model model, @AuthenticationPrincipal UserDetails UserDetails){
		List<MyOrder> orderList = myOrderManagement.sortByDate(myOrderManagement.findByUsername(UserDetails.getUsername(), myOrderRepository.findAll()));

		if(lastSortDateValueKonto.equals("neueste")){
			lastSortDateValueKonto = "älteste";
		}else {
			orderList = orderList.reversed();
			lastSortDateValueKonto = "neueste";
		}

		model.addAttribute("orderList", orderList);
		model.addAttribute("sortDateButtonValueKonto", lastSortDateValueKonto);
		return "my-orders";
	}



}

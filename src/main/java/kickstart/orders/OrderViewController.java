package kickstart.orders;

import kickstart.Inventory.Book;
import kickstart.Inventory.Merch;
import kickstart.Inventory.Calendar;
import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import kickstart.catalog.CatalogController;
import kickstart.user.User;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@SessionAttributes({"orderStates", "paymentMethods"})
public class OrderViewController {
	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;
	private final ShopProductCatalog shopProductCatalog;
	private final CatalogController catalogController;
	private final String[] orderStates = {"Alle", "Offen", "Abholbereit", "Abgeschlossen", "in Lieferung", "geliefert"};
	private final String[] paymentMethods = {"Alle", "Bar", "Rechnung"};

	private String[] lastFilterOptions = {"Alle", "Alle", "", ""};
	private String lastSortDateValue;
	private String lastSortDateValueKonto;

	@Autowired
	private MessageSource messageSource;

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement, ShopProductCatalog shopProductCatalog, CatalogController catalogController){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
		this.shopProductCatalog = shopProductCatalog;
		this.catalogController = catalogController;
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

	@GetMapping("/product/{id}")
	public String showProduct(@PathVariable Product.ProductIdentifier id, Model model) {
		ShopProduct product = shopProductCatalog.findById(id).get();

		if(product instanceof Book){
			return catalogController.bookDetail(product, model);
		}else if(product instanceof Calendar){
			return catalogController.calenderDetail(product, model);
		}else{
			return catalogController.merchDetail(product, model);
		}
	}

	/*
	This HAS to be in order controller and NOT in UserController even though it would be more fitting, because a cycle would be formed:
	order -> user -> order
	 */
	@PostMapping("filterCustomers")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	String filterCustomers(@RequestParam("filterState") String state, @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName, @RequestParam(value = "customerEmail", required = false, defaultValue = "") String customerEmail, Model model){
		Set<User> customers = myOrderManagement.getFilteredCustomersByStateOfOrders(state);
		customers = myOrderManagement.filterCustomers(customers, customerName, customerEmail);
		customers.removeIf(customer -> customer.getHighestRole() == null || !customer.getHighestRole().equals(Role.of("CUSTOMER")));

		String states = messageSource.getMessage("order.states", null, LocaleContextHolder.getLocale());

		model.addAttribute("customers", customers);
		model.addAttribute("selectedState", state);
		model.addAttribute("statesList", states.split(","));

		return "customer-overview";
	}
}

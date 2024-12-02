package kickstart.orders;

import jakarta.servlet.http.HttpServletRequest;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Controller
@SessionAttributes({"orderStates", "orderList", "selectedState"})
public class OrderViewController {
	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;
	private final String[] orderStates = {"Alle", "Offen", "Abholbereit", "Abgeschlossen", "in Lieferung", "geliefert"};

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
	}

	@ModelAttribute("orderStates")
	String[] initalizeOrderStates() {
		return this.orderStates;
	}

	/*
	@ModelAttribute("selectedState")
	String initalizeSelectedState() {
		return "Alle";
	}
	*/

	@GetMapping("/order-overview")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	String orderOverview(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());

		model.addAttribute("selectedState", "Alle");
		//temporär, später: filterOrders(...)
		return "order-overview";
	}


	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		//"Bist du sicher?"-Message einbauen

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

	@PostMapping("/filterByStatus")
	String filterByState(Model model, @RequestParam("valueStatus") String state){
		model.addAttribute("orderList", myOrderManagement.findByStatus(state, myOrderRepository.findAll()));
		model.addAttribute("selectedState", state);

		return "order-overview";
	}

	@PostMapping("/filterByProduct")
	String filterByProduct(@RequestParam(value = "productName", required = true) String productName, @RequestParam(value = "productId", required = true) Product.ProductIdentifier productId, Model model){
		Iterable<MyOrder> orderList;
		if(productId != null && !productId.toString().isEmpty()){
			orderList = myOrderManagement.findByProductId(productId, (Iterable<MyOrder>) model.getAttribute("orderList"));
		}else if(productName != null && !productName.isEmpty()){
			orderList = myOrderManagement.findByProductName(productName, (Iterable<MyOrder>) model.getAttribute("orderList"));
		}else{
			orderList = myOrderRepository.findAll();
		}

		model.addAttribute("orderList", orderList);

		return "order-overview";
	}

	@PostMapping("/filterOrders")
	String filterOrders(@RequestParam("state") String state, @RequestParam("productId") Product.ProductIdentifier productId, @RequestParam("userId") String userId){
		return "cart";
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
		MyOrder order = myOrderManagement.findByID(orderId);

		model.addAttribute("order", order);

		return "order-details";
	}

	@GetMapping("/order-details")
	String orderDetails(){
		return "order-details";
	}
}

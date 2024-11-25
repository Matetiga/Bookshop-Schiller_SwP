package kickstart.orders;

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
import java.util.Iterator;

@Controller
@SessionAttributes("orderStates")
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;
	private final String[] orderStates = {"ALL", "OPEN", "PAID", "COMPLETED"};

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
	}

	@ModelAttribute("orderStates")
	String[] initalizeOrderStates() {
		return this.orderStates;
	}

	@GetMapping("/order-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String orderOverview(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());
		model.addAttribute("selectedState", "ALL");
		return "order-overview";
	}

	@PostMapping("/showProducts")
	String showProducts(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		//model.addAttribute("show", true);
		//model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		myOrderRepository.deleteById(orderId);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/changeStatus")
	String changeStatus(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		MyOrder order = myOrderRepository.findById(orderId).get();
		order.changeStatus();
		myOrderRepository.save(order);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/filterByStatus")
	String filterStatus(Model model, @RequestParam("valueStatus") String status){
		Iterable<MyOrder> orderList;
		if(status == null || status.equals("ALL")){
			orderList = myOrderRepository.findAll();
		}
		else{
			orderList = myOrderManagement.findByStatus(OrderStatus.valueOf(status));
		}
		model.addAttribute("orderList", orderList);
		model.addAttribute("selectedState", status);
		return "order-overview";
	}

	@PostMapping("/filterByProduct")
	String filterByProduct(@RequestParam(value = "productName", required = false) String productName, @RequestParam(value = "productId", required = false) Product.ProductIdentifier productId, Model model){
		Iterable<MyOrder> orderList;
		if(productId != null){
			orderList = myOrderManagement.findByProductId(productId);
		}else if(productName != null){
			orderList = myOrderManagement.findByProductName(productName);
		}else{
			orderList = myOrderRepository.findAll();
		}
		model.addAttribute("orderList", orderList);
		return "order-overview";
	}

	@PostMapping("/sortByDate")
	String sortByDate(Model model){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		myOrderRepository.findAll().forEach(orderList::add);
		Collections.sort(orderList, Comparator.comparing(Order::getDateCreated));
		model.addAttribute("orderList", orderList);
		return "order-overview";
	}

	@GetMapping("/order/{orderID}")
	public String orderDetailsByIDAdmin(@PathVariable("orderID") Order.OrderIdentifier orderId, Model model) {
		MyOrder order = myOrderManagement.findByID(orderId);

		model.addAttribute("order", order);

		return "order-details";
	}
}

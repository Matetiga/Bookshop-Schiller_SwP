package kickstart.orders;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;

@Controller
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;
	private final MyOrderManagement myOrderManagement;

	public OrderViewController(MyOrderRepository myOrderRepository, MyOrderManagement myOrderManagement){
		this.myOrderRepository = myOrderRepository;
		this.myOrderManagement = myOrderManagement;
	}

	@GetMapping("/order-overview")
	String orderOverview(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/showProducts")
	String showProducts(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		//model.addAttribute("show", true);
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

	@PostMapping("/sortByDate")
	String sortByDate(Model model){
		//ArrayList<MyOrder> orderList = myOrderRepository.findAll().iterator();
		//orderList.sort(Comparator);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/filterByStatus")
	String filterStatus(Model model, @RequestParam(value = "valueStatus", required = false) String status){
		Iterable<MyOrder> orderList;
		if(status == null || status.equals("ALL")){
			orderList = myOrderRepository.findAll();
		}
		else{
			orderList = myOrderManagement.findByStatus(OrderStatus.valueOf(status));
		}
		model.addAttribute("orderList", orderList);
		return "order-overview";
	}

}

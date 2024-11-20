package kickstart.orders;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;
	private final OrderManagement orderManagement;

	public OrderViewController(MyOrderRepository myOrderRepository, OrderManagement orderManagement){
		this.myOrderRepository = myOrderRepository;
		this.orderManagement = orderManagement;
	}

	@GetMapping("/order-overview")
	String orderOverview(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		myOrderRepository.deleteById(orderId);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/order-overview")
	String filterStatus(Model model, @RequestParam(value = "valueStatus", required = false) String status){
		Iterable<MyOrder> orderList;
		if(status == null || status.equals("ALL")){
			orderList = myOrderRepository.findAll();
		}
		else{
			orderList = orderManagement.findByStatus(OrderStatus.valueOf(status));
		}
		model.addAttribute("orderList", orderList);
		return "order-overview";
	}
}

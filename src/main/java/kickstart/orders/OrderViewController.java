package kickstart.orders;

import org.salespointframework.order.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;

	public OrderViewController(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
	}

	@GetMapping("/order-overview")
	String addOrders(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}

	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		myOrderRepository.deleteById(orderId);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "order-overview";
	}
}

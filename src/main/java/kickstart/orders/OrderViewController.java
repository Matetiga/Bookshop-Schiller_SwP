package kickstart.orders;

import org.salespointframework.order.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;

	public OrderViewController(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
	}

	@GetMapping("/orderview")
	String addOrders(Model model){
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "orderview";
	}

	@PostMapping("/deleteOrder")
	private String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		myOrderRepository.deleteById(orderId);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "orderview";
	}
}

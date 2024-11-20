package kickstart.orders;

import org.salespointframework.order.Order;
import org.springframework.data.util.Streamable;
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
	String addOrders(Model model, @RequestParam(value = "valueStatus", required = false) String status){
		Iterable<MyOrder> orderList;
		if(status == null){
			orderList = myOrderRepository.findAll();
		}
		else{
			switch (status){
				case "OPEN":
					myOrderRepository.fin
			}
		}
		model.addAttribute("orderList", orderList);
		return "orderview";
	}

	@PostMapping("/deleteOrder")
	String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId, Model model){
		myOrderRepository.deleteById(orderId);
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "orderview";
	}
}

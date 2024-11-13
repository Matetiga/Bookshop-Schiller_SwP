package kickstart.orders;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.querydsl.core.annotations.Generated;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.QUserAccount;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.stream.Stream;

@Controller
@SessionAttributes("orderList")
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;

	public OrderViewController(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
	}

	@ModelAttribute("orderList")
	ArrayList<MyOrder> initializeCart() {
		return  new ArrayList<>();
	}

	@PostMapping("/orderview")
	public void addOrders(@ModelAttribute ArrayList<MyOrder> orderList){
		for(MyOrder myOrder : myOrderRepository.findAll()){
			orderList.add(myOrder);
		}
	}

	@PostMapping("/deleteOrder")
	private String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId){
		myOrderRepository.deleteById(orderId);
		return "orderview";
	}

	@GetMapping("/orderview")
	private String orders(@ModelAttribute ArrayList<MyOrder> orderList){
		System.out.println(orderList);
		return "orderview";
	}
}

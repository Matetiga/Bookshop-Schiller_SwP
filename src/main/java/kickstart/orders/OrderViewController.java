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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.stream.Stream;

@Controller
//@SessionAttributes("orderList")
public class OrderViewController {

	private final MyOrderRepository myOrderRepository;
	//private ArrayList<MyOrder> orderList;
	public OrderViewController(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
		//this.orderList = new ArrayList<>();
	}
/*
	@ModelAttribute("orderList")
	ArrayList<MyOrder> initializeCart() {
		return  new ArrayList<>();
	}
*/
	@GetMapping("/orderview")
	String addOrders(Model model){
		/*
		for(MyOrder myOrder : myOrderRepository.findAll()){
			orderList.add(myOrder);
		}
		System.out.println("orderList:");
		System.out.println(orderList);
		 */
		System.out.println("orderList:");
		System.out.println(myOrderRepository.findAll());
		model.addAttribute("orderList", myOrderRepository.findAll());
		return "orderview";
	}

	@PostMapping("/deleteOrder")
	private String deleteOrder(@RequestParam("orderId") Order.OrderIdentifier orderId){
		myOrderRepository.deleteById(orderId);
		return "orderview";
	}

	//@GetMapping("/orderview") private String orders(@ModelAttribute ArrayList<MyOrder> orderList){return "orderview";}
}

package kickstart.orders;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@SessionAttributes("stock")
public class OrderViewController {
	private ArrayList<Order> orderList = new ArrayList<>();

	@ModelAttribute("stock")
	ArrayList<Order> initializeCart() {
		return orderList;
	}

	public ArrayList<Order> getOrderList(){
		return orderList;
	}

	public void addOrder(Order order){
		orderList.add(order);
	}

	@PostMapping("/addOrder")
	private String addOrder(@ModelAttribute ArrayList<Order> stock){
		stock.add(new Order(UserAccount.UserAccountIdentifier.of("01")));
		return "orderview";
	}

	@GetMapping("/orderview")
	private String orders(){
		return "orderview";
	}
}

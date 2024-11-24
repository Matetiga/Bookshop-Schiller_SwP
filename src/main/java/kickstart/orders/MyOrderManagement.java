package kickstart.orders;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;


	MyOrderManagement(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
	}

	Iterable<MyOrder> findByStatus(OrderStatus filterStatus){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : myOrderRepository.findAll()){
			if(order.getMyOrderStatus() == filterStatus){
				orderList.add(order);
			}
		}
		return orderList;
	}

	//in Arbeit:
	Iterable<MyOrder> findByUser(UserAccount userAccount){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : myOrderRepository.findAll()){
			if(order.getUserAccountIdentifier() == userAccount.getId()){
				orderList.add(order);
			}
		}
		return orderList;
	}

	public MyOrder findByID(Order.OrderIdentifier id) {
		return myOrderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
	}
}

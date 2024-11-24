package kickstart.orders;


//import kickstart.user.UserRepository;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;
	//private final UserRepository userRepository;


	MyOrderManagement(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
		//this.userRepository = userRepository;
	}

	public void initalizeTestOrders(){
		//myOrderRepository.save(new MyOrder
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

	/*
	Iterable<MyOrder> findByCustomersName(String firstname, String lastname){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : myOrderRepository.findAll()){
			if(userRepository.findById(order.getUserAccountIdentifier())){
				orderList.add(order);
			}
		}
		return orderList;
	}
	 */
}

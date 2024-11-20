package kickstart.orders;

import org.salespointframework.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class OrderManagement {
	private final MyOrderRepository myOrderRepository;

	OrderManagement(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
	}

	Iterable<MyOrder> findByStatus(OrderStatus filterStatus){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : myOrderRepository.findAll()){
			if(order.getOrderStatus() == filterStatus){
				orderList.add(order);
			}
		}
		return orderList;
	}
}

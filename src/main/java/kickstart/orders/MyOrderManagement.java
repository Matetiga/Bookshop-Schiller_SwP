package kickstart.orders;

import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import kickstart.user.UserManagement;
import org.salespointframework.order.Order;

import org.salespointframework.catalog.Product;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;
	private final UserManagement userManagement;
	private final ShopProductCatalog shopProductCatalog;

	MyOrderManagement(MyOrderRepository myOrderRepository, UserManagement userManagement, ShopProductCatalog shopProductCatalog){
		this.myOrderRepository = myOrderRepository;
		this.userManagement = userManagement;
		this.shopProductCatalog = shopProductCatalog;
	}

	public Iterable<MyOrder> findByStatus(String state, Iterable<MyOrder> filteredList){
		if(state == null || state.equals("Alle")){
			return filteredList;
		}else{
			ArrayList<MyOrder> orderList = new ArrayList<>();
			for(MyOrder order : filteredList){
				if(order.getMyOrderStatus().equals(state)){
					orderList.add(order);
				}
			}
			return orderList;
		}
	}

	public Iterable<MyOrder> findByPaymentMethod(String paymentMethod, Iterable<MyOrder> list){
		if(paymentMethod == null || paymentMethod.equals("Alle")){
			return list;
		}else {
			ArrayList<MyOrder> orderList = new ArrayList<>();
			for (MyOrder order : list) {
				if (order.getStringPaymentMethod().equals(paymentMethod)) {
					orderList.add(order);
				}
			}
			return orderList;
		}
	}

	public Iterable<MyOrder> findByProductName(String productName, Iterable<MyOrder> filteredList){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : filteredList){
			for(OrderLine orderLine : order.getOrderLines()){
				if(orderLine.getProductName().equals(productName)){
					orderList.add(order);
				}
			}
		}
		return orderList;
	}

	public Iterable<MyOrder> findByProductId(Product.ProductIdentifier productId, Iterable<MyOrder> filteredList){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : filteredList){
			for(OrderLine orderLine : order.getOrderLines()){
				if(orderLine.getProductIdentifier().equals(productId)){
					orderList.add(order);
				}
			}
		}
		return orderList;
	}

	public Iterable<MyOrder> findByUsername(String username, Iterable<MyOrder> list){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : list){
			if(userManagement.findByUsername(username) == userManagement.findByID(UUID.fromString(order.getUserAccountIdentifier().toString()))){
				orderList.add(order);
			}
		}
		return orderList;
	}

	public Iterable<MyOrder> findByMonth(Month month, Iterable<MyOrder> list){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : list){
			if(month == order.getDebitTime().getMonth()){
				orderList.add(order);
			}
		}
		return orderList;
	}

	public double getTotalOfOrderList(Iterable<MyOrder> orderList){
		double sum = 0;
		for(MyOrder order : orderList){
			sum += order.getTotal().getNumber().doubleValueExact();
		}
		return sum;
	}

	public double getTotalOfMonth(Month month, Iterable<MyOrder> orderList){
		return this.getTotalOfOrderList(this.findByMonth(month, orderList));
	}

	public void setDeliveryState(Iterable<MyOrder> orderList){
		for(MyOrder order : this.findByStatus("in Lieferung", orderList)){
			if(LocalDateTime.now().minusDays(3).isAfter(order.getStartDeliveryTime())){
				order.changeStatus();
			}
		}
	}

	public void initializeRandomOrders(){
		for (int i = 0; i < 50; i++){
			Random random = new Random();
			List<ShopProduct> shopProductList = shopProductCatalog.findAll().stream().toList();

			MyOrder randomOrder = new MyOrder(userManagement.findByUsername("employee2@example.com"), random.nextBoolean() ? "Bar" : "Rechnung");

			for (int j = 0; j < 5; j++){
				ShopProduct randomProduct = shopProductList.get(random.nextInt(shopProductList.size()));

				randomOrder.addOrderLine(randomProduct, Quantity.of(random.nextInt(1, 11)));
			}

			randomOrder.setDebitTime(LocalDateTime.of(random.nextInt(2023, 2025), random.nextInt(12) + 1, random.nextInt(1, 29), random.nextInt(0, 24), random.nextInt(0, 60)));
			randomOrder.setState("Abgeschlossen");

			myOrderRepository.save(randomOrder);
		}
	}

	public MyOrder findByID(Order.OrderIdentifier id) {
		return myOrderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
	}
}

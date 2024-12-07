package kickstart.orders;

import kickstart.user.UserManagement;
import org.salespointframework.order.Order;

import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static kickstart.Inventory.Genre.createGenre;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;
	private final UserManagement userManagement;

	MyOrderManagement(MyOrderRepository myOrderRepository, UserManagement userManagement){
		this.myOrderRepository = myOrderRepository;
		this.userManagement = userManagement;
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
			if(month == order.getDateCreated().getMonth()){
				orderList.add(order);
			}
		}
		return orderList;
	}

	public long getTotalOfOrderList(Iterable<MyOrder> orderList){
		long sum = 0;
		for(MyOrder order : orderList){
			sum += order.getTotal().getNumber().longValue();
		}
		return sum;
	}

	public long getTotalOfMonth(Month month, Iterable<MyOrder> orderList){
		return this.getTotalOfOrderList(this.findByMonth(month, orderList));
	}

	public void setDeliveryState(Iterable<MyOrder> orderList){
		for(MyOrder order : this.findByStatus("in Lieferung", orderList)){
			if(LocalDateTime.now().minusDays(3).isAfter(order.getStartDeliveryTime())){
				order.changeStatus();
			}
		}
	}
	public void initalizeDemoOrders(){
		MyOrder testOrder1 = new MyOrder(userManagement.findByUsername("employee2@example.com"), "Rechnung");
		MyOrder testOrder2 = new MyOrder(userManagement.findByUsername("employee2@example.com"), "Bar");
		MyOrder testOrder3 = new MyOrder(userManagement.findByUsername("employee2@example.com"), "Bar");
		//MyOrder testOrder3 = new MyOrder(UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString()), "Bar");

		Genre fiction = createGenre("Fiction");
		Genre history = createGenre("Cooking");

		Set<Genre> genreSet1 = new HashSet<>(Set.of(fiction));
		Set<Genre> genreSet2 = new HashSet<>(Set.of(history));

		Product exampleProduct1 = new Book("Frankreich", "gatsby.jpg", Money.of(10 ,"EUR"),
			"Test", genreSet1, "Arno Dübel",
			"9780743273565", "Scribner");
		Product exampleProduct2 = new Book("spannendes Buch", "sapiens.jpg", Money.of(15, "EUR"),
			"Ich weiß ich nicht", genreSet2, "Karin Ritter",
			"9780062316110", "Harper");
		Product exampleProduct3 = new Book("S-Bahn-Netz2024", "sapiens.jpg", Money.of(15, "EUR"),
			"123", genreSet2, "Rainer Winkler",
			"9780062316110", "Harper");

		testOrder1.addOrderLine(exampleProduct1, Quantity.of(1));
		testOrder1.addOrderLine(exampleProduct2, Quantity.of(42));
		testOrder1.addOrderLine(exampleProduct3, Quantity.of(3));

		testOrder2.addOrderLine(exampleProduct1, Quantity.of(7));

		testOrder3.addOrderLine(exampleProduct2, Quantity.of(1));

		myOrderRepository.save(testOrder1);
		myOrderRepository.save(testOrder2);
		myOrderRepository.save(testOrder3);

	}

	public MyOrder findByID(Order.OrderIdentifier id) {
		return myOrderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
	}
}

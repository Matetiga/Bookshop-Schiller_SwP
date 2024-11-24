package kickstart.orders;

import org.salespointframework.order.Order;

import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

import static kickstart.Inventory.Genre.createGenre;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;

	MyOrderManagement(MyOrderRepository myOrderRepository){
		this.myOrderRepository = myOrderRepository;
		initalizeDemoOrders();

	}

	public Iterable<MyOrder> findByStatus(OrderStatus filterStatus){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : myOrderRepository.findAll()){
			if(order.getMyOrderStatus() == filterStatus){
				orderList.add(order);
			}
		}
		return orderList;
	}

	public void initalizeDemoOrders(){
		MyOrder testOrder1 = new MyOrder(UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString()), "Rechnung");
		MyOrder testOrder2 = new MyOrder(UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString()), "Bar");
		MyOrder testOrder3 = new MyOrder(UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString()), "Bar");

		Genre fiction = createGenre("Fiction");
		Genre history = createGenre("Cooking");

		Product exampleProduct1 = new Book("Abarth", "gatsby.jpg", Money.of(10 ,"EUR"),
			"Mir gehts Abartig schlecht", fiction, "Jemand mit einem schönen Nachnamen",
			"9780743273565", "Scribner");
		Product exampleProduct2 = new Book("Bisschen verwirrend vielleicht am Anfang", "sapiens.jpg", Money.of(15, "EUR"),
			"Ich weiß es doch auch nicht", history, "W. Lederle",
			"9780062316110", "Harper");
		Product exampleProduct3 = new Book("S-Bahn", "sapiens.jpg", Money.of(15, "EUR"),
			"123", history, "Rainer Winkler",
			"9780062316110", "Harper");

		testOrder1.addOrderLine(exampleProduct1, Quantity.of(69));
		testOrder1.addOrderLine(exampleProduct2, Quantity.of(42));
		testOrder1.addOrderLine(exampleProduct3, Quantity.of(3));

		testOrder2.addOrderLine(exampleProduct1, Quantity.of(7));

		testOrder3.addOrderLine(exampleProduct2, Quantity.of(1));

		myOrderRepository.save(testOrder1);
		myOrderRepository.save(testOrder2);
		myOrderRepository.save(testOrder3);

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

	public MyOrder findByID(Order.OrderIdentifier id) {
		return myOrderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
	}
}

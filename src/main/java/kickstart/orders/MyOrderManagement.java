package kickstart.orders;

import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.jetbrains.annotations.NotNull;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MyOrderManagement {
	private final MyOrderRepository myOrderRepository;

	private final UserManagement userManagement;

	private final ShopProductCatalog shopProductCatalog;

	MyOrderManagement(MyOrderRepository myOrderRepository, UserManagement userManagement,
					  ShopProductCatalog shopProductCatalog){
		this.myOrderRepository = myOrderRepository;
		this.userManagement = userManagement;
		this.shopProductCatalog = shopProductCatalog;
	}

	/**
	 *
	 * @param state
	 * @param filteredList
	 * find Orders by their State
	 * @return orderList
	 */
	public Iterable<MyOrder> findByStatus(String state, Iterable<MyOrder> filteredList){
		if(state == null || state.equals("Alle") || state.equals("All")){
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

	/**
	 *
	 * @param paymentMethod
	 * @param list
	 * find orders by their payment method
	 * @return orderList
	 */
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

	/**
	 *
	 * @param productName
	 * @param filteredList
	 * find orders by product name (of product which was in the order)
	 * @return orderList
	 */
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

	/**
	 *
	 * @param username
	 * @param list
	 * find orders by username of the user who created the order
	 * @return orderList
	 */
	public Iterable<MyOrder> findByUsername(String username, Iterable<MyOrder> list){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : list){
			if(userManagement.findByUsername(username) == userManagement.findByID(
				UUID.fromString(order.getUserAccountIdentifier().toString()))){
				orderList.add(order);
			}
		}
		return orderList;
	}

	/**
	 *
	 * @param month
	 * @param year
	 * @param list
	 * find orders by month and year of placement
	 * @return
	 */
	public Iterable<MyOrder> findByMonthAndYear(Month month, int year, Iterable<MyOrder> list){
		ArrayList<MyOrder> orderList = new ArrayList<>();
		for(MyOrder order : list){
			if(month == order.getDebitTime().getMonth() && year == order.getDebitTime().getYear()){
				orderList.add(order);
			}
		}
		return orderList;
	}

	/**
	 *
	 * @param orderList
	 * calculates total of an order
	 * @return sum (Money)
	 */
	public double getTotalOfOrderList(Iterable<MyOrder> orderList){
		double sum = 0;
		for(MyOrder order : orderList){
			sum += order.getTotal().getNumber().doubleValueExact();
		}
		return sum;
	}

	/**
	 *
	 * @param month
	 * @param year
	 * @param orderList
	 * calculates total of all order of one month
	 * @return  sum (Money)
	 */
	public double getTotalOfMonthAndYear(Month month, int year, Iterable<MyOrder> orderList){
		return this.getTotalOfOrderList(this.findByMonthAndYear(month, year, orderList));
	}

	/**
	 *
	 * @param orderList
	 * used to set delivery time (changes order state from "in Lieferung" to "geliefert" after set time)
	 */
	public void setDeliveryState(Iterable<MyOrder> orderList){
		for(MyOrder order : this.findByStatus("in Lieferung", orderList)){
			if(LocalDateTime.now().minusSeconds(30).isAfter(order.getStartDeliveryTime())){
				order.changeStatus();
			}
		}
	}

	/**
	 *
	 * @param state
	 * @param paymentMethod
	 * @param productName
	 * @param username
	 * filter orders
	 * @return filtered list
	 */
	public Iterable<MyOrder> filterAllOrders(String state, String paymentMethod, String productName, String username){
		Iterable<MyOrder> filterList = this.findByStatus(state, myOrderRepository.findAll());
		filterList = this.findByPaymentMethod(paymentMethod, filterList);

		if (!productName.isEmpty()) {
			filterList = this.findByProductName(productName, filterList);
		}
		if (!username.isEmpty()){
			filterList = this.findByUsername(username, filterList);
		}

		return filterList;
	}

	/**
	 *
	 * @param iterable
	 * sorts orders by date
	 * @return sorted list
	 */
	public List<MyOrder> sortByDate(Iterable<MyOrder> iterable){
		List<MyOrder> list = new ArrayList<>();
		iterable.forEach(list::add);
		list.sort(Comparator.comparing(MyOrder::getDebitTime));

		return list;
	}

	/**
	 * crates random example orders
	 */
	public void initializeRandomOrders(){
		for (int i = 0; i < 300; i++){
			Random random = new Random();
			List<ShopProduct> shopProductList = shopProductCatalog.findAll().stream().toList();

			MyOrder randomOrder = new MyOrder(userManagement.findByUsername(random.nextBoolean() ?
				"customer1@example.com" : "customer2@example.com"), random.nextBoolean() ? "Bar" : "Rechnung");

			for (int j = 0; j < 5; j++){
				ShopProduct randomProduct;
				do{
					randomProduct = shopProductList.get(random.nextInt(shopProductList.size()));

				}while (!randomOrder.getOrderLines(randomProduct).isEmpty());
				randomOrder.addOrderLine(randomProduct, Quantity.of(random.nextInt(1, 4)));
			}

			randomOrder.setDebitTime(LocalDateTime.of(random.nextInt(2023, 2025),
				random.nextInt(12) + 1, random.nextInt(1, 29), random.nextInt(0, 24), random.nextInt(0, 60)));
			randomOrder.setState("Abgeschlossen");

			myOrderRepository.save(randomOrder);
		}
	}

	/**
	 *
	 * @param id
	 * finds an order by its id
	 * @return order
	 */
	public MyOrder findByID(Order.OrderIdentifier id) {
		return myOrderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
	}

	/**
	 *
	 * @param state
	 * For customer-overview we can filter for customers who have orders of some state
	 * @return streamable users
	 */
	public Set<User> getFilteredCustomersByStateOfOrders(@NotNull String state){
		if (state.equals("Alle") || state.equals("All")){
			Streamable<User> users= userManagement.findAll();
			return users.stream().collect(Collectors.toSet());
		}

		Iterable<MyOrder> orders = this.findByStatus(state, myOrderRepository.findAll());

		Set<User> users = new HashSet<>();

		for (MyOrder order : orders) {
			users.add(order.getUser());
		}

		return users;
	}

	/**
	 *
	 * @param users
	 * @param name
	 * @param email
	 * filters Customers
	 * @return Set of users
	 */
	public Set<User> filterCustomers(Set<User> users, String name, String email){
		return userManagement.filterCustomers(users, name, email);
	}
}

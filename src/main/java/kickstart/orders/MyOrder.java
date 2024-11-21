package kickstart.orders;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;
	private OrderStatus myOrderStatus;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod) {
		super(userId);
		this.stringPaymentMethod = paymentMethod;
		this.myOrderStatus = OrderStatus.OPEN;
	}

	public MyOrder(){

	}

	public String getStringPaymentMethod(){
		return stringPaymentMethod;
	}

	public OrderStatus getMyOrderStatus(){
		return  this.myOrderStatus;
	}

	public void changeStatus(){
		switch (this.getMyOrderStatus()){
			case OPEN:
				this.myOrderStatus = OrderStatus.PAID;
				break;
			case PAID:
				this.myOrderStatus = OrderStatus.COMPLETED;
				break;
			case COMPLETED:
				this.myOrderStatus = OrderStatus.OPEN;
		}
	}
}

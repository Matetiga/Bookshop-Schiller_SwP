package kickstart.orders;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;
	//TODO testing for The getTotal method to work and get te total price of the order
	// GPT added @Embedded so the public MonetaryAmount getTotalOrder() method can work
	@Embedded
	private MonetaryAmount total;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod, MonetaryAmount total) {
		super(userId);
		this.stringPaymentMethod = paymentMethod;
		this.total = total;
	}

	public MyOrder(){

	}

	public String getStringPaymentMethod(){
		return stringPaymentMethod;
	}

	public MonetaryAmount getTotalOrder(){
		return total;
	}
}

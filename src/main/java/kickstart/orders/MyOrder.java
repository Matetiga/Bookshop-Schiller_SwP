package kickstart.orders;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod) {
		super(userId);
		this.stringPaymentMethod = paymentMethod;
	}

	public MyOrder(){

	}

	public String getStringPaymentMethod(){
		return stringPaymentMethod;
	}
}

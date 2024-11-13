package kickstart.orders;

import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import java.io.Serializable;

@Entity
public class MyOrder extends Order {
	private String paymentMethod;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod){
		super(userId);
		this.paymentMethod = paymentMethod;

	}

	public MyOrder(){

	}
}

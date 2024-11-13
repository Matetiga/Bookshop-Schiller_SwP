package kickstart.orders;

import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class MyOrder extends Order {
	private final String paymentMethod;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod){
		super(userId);
		this.paymentMethod = paymentMethod;

	}
}

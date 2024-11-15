package kickstart.orders;

import jakarta.persistence.Entity;
import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import java.io.Serializable;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod){
		super(userId);
		this.stringPaymentMethod = paymentMethod;

	}

	public MyOrder(){

	}

	public String getStringPaymentMethod(){
		return this.stringPaymentMethod;
	}
}

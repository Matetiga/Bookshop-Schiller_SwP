package kickstart.orders;

import jakarta.persistence.*;
import kickstart.user.User;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;
	private String myOrderStatus;

	@ManyToOne
	private User user;

	public MyOrder(User user, String paymentMethod) {
		super(user.getUserAccount().getId());
		this.user = user;
		this.stringPaymentMethod = paymentMethod;
		this.myOrderStatus = "Offen";
	}

	public MyOrder(){

	}

	public User getUser(){
		return this.user;
	}

	public String getStringPaymentMethod(){
		return this.stringPaymentMethod;
	}

	public String getMyOrderStatus(){
		return  this.myOrderStatus;
	}

	public void changeStatus(){
		if(this.getStringPaymentMethod().equals("Bar")){
			switch (this.getMyOrderStatus()){
				case "Offen":
					this.myOrderStatus = "Abholbereit";
					break;
				case "Abholbereit":
					this.myOrderStatus = "Abgeschlossen";
					break;
				case "Abgeschlossen":
					this.myOrderStatus = "Offen";
					break;
			}
		}else if(this.getStringPaymentMethod().equals("Rechnung")) {
			switch (this.getMyOrderStatus()) {
				case "Offen":
					this.myOrderStatus = "in Lieferung";
					break;
				case "in Lieferung":
					this.myOrderStatus = "geliefert";
					break;
				case "geliefert":
					this.myOrderStatus = "Abgeschlossen";
					break;
				case "Abgeschlossen":
					this.myOrderStatus = "Offen";
			}
		}
	}
}

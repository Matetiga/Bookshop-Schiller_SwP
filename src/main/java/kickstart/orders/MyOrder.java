package kickstart.orders;

import jakarta.persistence.*;
import kickstart.user.User;
import org.salespointframework.order.Order;

import java.time.LocalDateTime;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;
	private String myOrderStatus;
	private LocalDateTime startDeliveryTime;
	private LocalDateTime debitTime; //wird bis auf demoOrders, zur Bestellzeit gesetzt

	@ManyToOne
	private User user;

	public MyOrder(User user, String paymentMethod) {
		super(user.getUserAccount().getId());
		this.user = user;
		this.stringPaymentMethod = paymentMethod;
		this.myOrderStatus = "Offen";
		this.debitTime = LocalDateTime.now();
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

	public LocalDateTime getStartDeliveryTime(){
		return this.startDeliveryTime;
	}

	public void setStartDeliveryTime(LocalDateTime time){
		this.startDeliveryTime = time;
	}

	public LocalDateTime getDebitTime(){
		return this.debitTime;
	}

	public void setDebitTime(LocalDateTime time){
		this.debitTime = time;
	}

	public void setState(String state){
		this.myOrderStatus = state;
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
					this.startDeliveryTime = LocalDateTime.now();
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

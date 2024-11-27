package kickstart.orders;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;

@Entity
public class MyOrder extends Order {
	private String stringPaymentMethod;
	private String myOrderStatus;

	public MyOrder(UserAccount.UserAccountIdentifier userId, String paymentMethod) {
		super(userId);
		this.stringPaymentMethod = paymentMethod;
		this.myOrderStatus = "Offen";
	}

	public MyOrder(){

	}

	public String getStringPaymentMethod(){
		return stringPaymentMethod;
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
		}else if(this.getStringPaymentMethod().equals("Rechnung")){
			switch (this.getMyOrderStatus()){
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
		}else{
			try {
				throw new Exception("Ungültige Bezahlmethode!");
			} catch (Exception e) {
				System.out.println("Fehler: " + e.getMessage());
			}
		}

	}
}

package kickstart.user;

import jakarta.persistence.Entity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.*;

@Entity
public class Customer extends User {

	public Customer(UserAccount userAccount, String address) {
		super(userAccount, address);
	}

}

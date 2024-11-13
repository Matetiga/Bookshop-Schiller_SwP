package kickstart.user;

import jakarta.persistence.Entity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.*;

@Entity
public class Admin extends User {

	public Admin(UserAccount userAccount, String address) {
		super(userAccount, address);
	}

}

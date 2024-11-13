package kickstart.user;

import jakarta.persistence.Entity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.*;

@Entity
public class Employee extends Admin{

	public Employee(UserAccount userAccount, String address) {
		super(userAccount, address);
	}

}

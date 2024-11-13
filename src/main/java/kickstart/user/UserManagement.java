package kickstart.user;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
@Transactional
public class UserManagement {

    public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");

    private final UserRepository users;
    private final UserAccountManagement userAccounts;

    UserManagement(UserRepository users, @Qualifier("persistentUserAccountManagement") UserAccountManagement userAccounts) {

        Assert.notNull(users, "UserRepository must not be null!");
        Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

        this.users = users;
        this.userAccounts = userAccounts;
    }

	public Customer createCustomer(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("CUSTOMER"));
		return users.save(new Customer(userAccount, form.getAddress()));
	}

	public Employee createEmployee(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("EMPLOYEE"));
		return users.save(new Employee(userAccount, form.getAddress()));
	}

	public Admin createAdmin(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("ADMIN"));
		return users.save(new Admin(userAccount, form.getAddress()));
	}

    private UserAccount createUserAccount(RegistrationForm form, Role role) {

        Assert.notNull(form, "Registration form must not be null!");

        if (userAccounts.findByUsername(form.getName()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        var password = UnencryptedPassword.of(form.getPassword());
        return userAccounts.create(form.getName(), password, role);

    }


    public Streamable<User> findAll() {
        return users.findAll();
    }
}

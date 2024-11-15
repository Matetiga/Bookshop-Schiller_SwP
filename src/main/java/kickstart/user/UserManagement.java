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

import java.util.UUID;


@Service
@Transactional
public class UserManagement {

    public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");

    private final UserRepository users;
    private final UserAccountManagement userAccounts;

    // warum @Qualifier("persistentUserAccountManagement")
    UserManagement(UserRepository users, @Qualifier("persistentUserAccountManagement") UserAccountManagement userAccounts) {

        Assert.notNull(users, "UserRepository must not be null!");
        Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

        this.users = users;
        this.userAccounts = userAccounts;
    }

	public User createCustomer(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("CUSTOMER"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

	public User createEmployee(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("EMPLOYEE"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

	public User createAdmin(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("ADMIN"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

    private UserAccount createUserAccount(RegistrationForm form, Role role) {

        Assert.notNull(form, "Registration form must not be null!");

        var password = UnencryptedPassword.of(form.getPassword());
        return userAccounts.create(form.getEmail(), password, role);

    }

    public Streamable<User> findAll() {
        return users.findAll();
    }

    public boolean findByEmail(String email) {
        return userAccounts.findByUsername(email).isPresent();
    }

	@Transactional
	public User findByID(UUID id) {
		for (var user : users.findAll()) {
			if (user.getUserAccount().getId().toString().equals(id.toString())) {
				return user;
			}
		}
		return null;
	}


	@Transactional
	public void promoteAccountById(UUID id) {
		User user = this.findByID(id);

		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))) return;

		if (user.getUserAccount().hasRole((Role.of("EMPLOYEE")))){
			user.getUserAccount().add(Role.of("ADMIN"));
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			return;
		}
		if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))){
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			user.getUserAccount().remove(Role.of("CUSTOMER"));
		}

	}

	@Transactional
	public void degradeAccountById(UUID id) {
		User user = this.findByID(id);

		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))){
			user.getUserAccount().remove(Role.of("ADMIN"));
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			return;
		}

		if (user.getUserAccount().hasRole((Role.of("EMPLOYEE")))){
			user.getUserAccount().add(Role.of("CUSTOMER"));
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			return;
		}

		if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))) users.delete(user);
	}
}
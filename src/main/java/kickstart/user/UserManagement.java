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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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

	public void editProfile(User user, UserAccount userAccount, EditUserProfilForm form) {
		user.setName(form.getEdit_name());
		user.setLast_name(form.getEdit_last_name());
		user.setAddress(form.getEdit_address());
		userAccounts.changePassword(userAccount, UnencryptedPassword.of(form.getEdit_password()));
	}

    private UserAccount createUserAccount(RegistrationForm form, Role role) {

        Assert.notNull(form, "Registration form must not be null!");

        var password = UnencryptedPassword.of(form.getPassword());
        return userAccounts.create(form.getEmail(), password, role);

    }

    public Streamable<User> findAll() {
        return users.findAll();
    }

    public boolean emailExistsAlready(String email) {
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
	public User findByUsername(String username) {
		for (var user : users.findAll()) {
			if (user.getUserAccount().getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}

	@Transactional
	public String promoteAccountById(UUID id) {
		User user = this.safeUserGetByID(id);

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))){
			return "Promotion failed: Cannot promote admin account further.";
		}
		if (user.getUserAccount().hasRole((Role.of("EMPLOYEE")))){
			user.getUserAccount().add(Role.of("ADMIN"));
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			return String.format("Promotion successful: Account '%s' has been promoted to Role of %s", id.toString(), Role.of("ADMIN"));
		}
		if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))){
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			user.getUserAccount().remove(Role.of("CUSTOMER"));
			return String.format("Promotion successful: Account '%s' has been promoted to Role of %s", id.toString(), Role.of("EMPLOYEE"));
		}

		return "Promotion failed: Account '%s' does not exist.";
	}

	@Transactional
	public String degradeAccountById(UUID id) {
		User user = this.safeUserGetByID(id);
		//This prevents admin1 for example to degrade himself
		if (currentUserSameAsID(id)){
			System.out.println("Thinks same id");
			return "Degradation failed: Cannot degrade own Account.";
		}

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))){
			user.getUserAccount().remove(Role.of("ADMIN"));
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			return String.format("Degradation successful: Account '%s' has been degraded to Role of %s.", id.toString(), Role.of("EMPLOYEE"));
		}

		if (user.getUserAccount().hasRole((Role.of("EMPLOYEE")))){
			user.getUserAccount().add(Role.of("CUSTOMER"));
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			return String.format("Degradation successful: Account '%s' has been degraded to Role of %s.", id.toString(), Role.of("CUSTOMER"));
		}

		if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))){
			users.delete(user);
			return String.format("Customer Account deleted successfully: Account '%s' has been deleted", id.toString());

		}
		return "Degradation failed: Account '%s' does not exist.";
	}

	public User safeUserGetByID(UUID id){
		User user = this.findByID(id);

		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}
		return user;
	}

	public boolean currentUserSameAsID(UUID id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User requestingUser = this.findByUsername(authentication.getName());
		User requestedUser = this.findByID(id);
		
		return requestedUser.equals(requestingUser);
	}
}
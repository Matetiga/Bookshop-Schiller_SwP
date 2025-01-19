package kickstart.user;

import org.jetbrains.annotations.NotNull;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import kickstart.Achievement.Achievement;


import java.util.*;


@Service
@Transactional
public class UserManagement {

    public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");

    private final UserRepository users;
    private final UserAccountManagement userAccounts;

	/**
	 *
	 * @param users
	 * @param userAccounts
	 */
	UserManagement(UserRepository users,
				   @Qualifier("persistentUserAccountManagement") UserAccountManagement userAccounts) {

        Assert.notNull(users, "UserRepository must not be null!");
        Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.users = users;
        this.userAccounts = userAccounts;
    }

	/**
	 *
	 * @param form
	 * @return
	 */
	public User createCustomer(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("CUSTOMER"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

	/**
	 *
	 * @param form
	 * @return
	 */
	public User createEmployee(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("EMPLOYEE"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

	/**
	 *
	 * @param form
	 * @return
	 */
	public User createAdmin(RegistrationForm form) {
		var userAccount = createUserAccount(form, Role.of("ADMIN"));
		return users.save(new User(userAccount, form.getAddress(), form.getName(), form.getLast_name(), form.getBirthDate()));
	}

	/**
	 *
	 * @param user
	 * @param userAccount
	 * @param form
	 */
	public void editProfile(User user, UserAccount userAccount, EditUserProfilForm form) {
		user.setName(form.getEdit_name());
		user.setLast_name(form.getEdit_last_name());
		user.setAddress(form.getEdit_address());
		userAccounts.changePassword(userAccount, UnencryptedPassword.of(form.getEdit_password()));
	}

	/**
	 *
	 * @param user
	 * @param form
	 */
	public void editProfilebyAuthority(User user, EditPersonbyAuthorityForm form) {
		user.setName(form.getnew_name());
		user.setLast_name(form.getnew_last_name());
		user.setAddress(form.getnew_address());
	}

	/**
	 *
	 * @param form
	 * @param role
	 * @return
	 */
    private UserAccount createUserAccount(RegistrationForm form, Role role) {

        Assert.notNull(form, "Registration form must not be null!");

        var password = UnencryptedPassword.of(form.getPassword());
        return userAccounts.create(form.getEmail(), password, role);

    }

	/**
	 *
	 * @return
	 */
    public Streamable<User> findAll() {
        return users.findAll();
    }

	/**
	 *
	 * @param email
	 * @return
	 */
    public boolean emailExistsAlready(String email) {
        return userAccounts.findByUsername(email).isPresent();
    }

	/**
	 *
	 * @param id
	 * @return
	 */
	@Transactional
	public User findByID(UUID id) {
		for (var user : users.findAll()) {
			if (user.getUserAccount().getId().toString().equals(id.toString())) {
				return user;
			}
		}
		return null;
	}

	/**
	 *
	 * @param username
	 * @return
	 */
	@Transactional
	public User findByUsername(String username) {
		for (var user : users.findAll()) {
			if (user.getUserAccount().getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Transactional
	public String promoteAccountById(UUID id) {
		User user = this.safeUserGetByID(id);

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))) {
			return "Promotion failed: Cannot promote admin account further.";
		}

		String promotionResult = null;

		if (user.getUserAccount().hasRole(Role.of("EMPLOYEE"))) {
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			user.getUserAccount().add(Role.of("ADMIN"));
			promotionResult = String.format("Promotion successful: Account '%s' has been promoted to Role of %s",
				id.toString(), Role.of("ADMIN"));
		} else if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))) {
			user.getUserAccount().remove(Role.of("CUSTOMER"));
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			promotionResult = String.format("Promotion successful: Account '%s' has been promoted to Role of %s",
				id.toString(), Role.of("EMPLOYEE"));
		}

		return promotionResult != null
			? promotionResult
			: String.format("Promotion failed: Account '%s' does not exist.", id.toString());
	}


	/**
	 *
	 * @param id
	 * @return
	 */
	@Transactional
	public String degradeAccountById(UUID id) {
		User user = this.safeUserGetByID(id);

		if (currentUserSameAsID(id)) {
			return "Degradation failed: Cannot degrade own Account.";
		}

		boolean accountDegraded = false;

		if (user.getUserAccount().hasRole(Role.of("ADMIN"))) {
			user.getUserAccount().remove(Role.of("ADMIN"));
			user.getUserAccount().add(Role.of("EMPLOYEE"));
			accountDegraded = true;
		} else if (user.getUserAccount().hasRole(Role.of("EMPLOYEE"))) {
			user.getUserAccount().remove(Role.of("EMPLOYEE"));
			user.getUserAccount().add(Role.of("CUSTOMER"));
			accountDegraded = true;
		} else if (user.getUserAccount().hasRole(Role.of("CUSTOMER"))) {
			users.delete(user);
			return String.format("Customer Account deleted successfully: Account '%s' has been deleted",
				id.toString());
		}

		return accountDegraded
			? String.format("Degradation successful: Account '%s' has been degraded!", id.toString())
			: String.format("Degradation failed: Account '%s' does not exist.", id.toString());
	}


	/**
	 *
	 * @param id
	 * @return
	 */
	public User safeUserGetByID(UUID id){
		User user = this.findByID(id);

		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}
		return user;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public boolean currentUserSameAsID(UUID id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User requestingUser = this.findByUsername(authentication.getName());
		User requestedUser = this.findByID(id);
		
		return requestedUser.equals(requestingUser);
	}

	/**
	 *
	 * @param customers
	 * @param name
	 * @param email
	 * @return
	 */
	public Set<User> filterCustomers(Set<User> customers, String name, String email){
		customers = this.filterByName(customers, name);
		customers = this.filterByEmail(customers, email);

		return customers;
	}

	/**
	 *
	 * @param users
	 * @param name
	 * @return
	 */
	private Set<User> filterByName(@NotNull Set<User> users, @NotNull String name) {
		String lowerStr1 = name.toLowerCase();
		Iterator<User> iterator = users.iterator();

		while (iterator.hasNext()) {
			User user = iterator.next();
			String lowerStr2 = user.getName().toLowerCase();
			if (!(lowerStr1.contains(lowerStr2) || lowerStr2.contains(lowerStr1))) {
				iterator.remove();
			}
		}
		return users;
	}

	/**
	 *
	 * @param users
	 * @param email
	 * @return
	 */
	private Set<User> filterByEmail(@NotNull Set<User> users, @NotNull String email) {
		String lowerStr1 = email.toLowerCase();
		Iterator<User> iterator = users.iterator();

		while (iterator.hasNext()) {
			User user = iterator.next();
			String lowerStr2 = user.getEmail().toLowerCase();
			if (!(lowerStr1.contains(lowerStr2) || lowerStr2.contains(lowerStr1))) {
				iterator.remove();
			}
		}
		return users;
	}

	/**
	 *
	 * @param role
	 * @return
	 */
	public List<User> getAllUsersOfRole(Role role){
		List<User> users = new ArrayList<>();

		for (User user: this.findAll()){
			if (user.getHighestRole().equals(role)){
				users.add(user);
			}
		}

		return users;
	}

	/**
	 *
	 * @param user
	 * @param achievement
	 */
	@Transactional
	public void addAchievementToUser(User user, Achievement achievement) {
		user.addAchievement(achievement);
	}

	/**
	 *
	 * @param userDetails
	 * @return
	 */
	@Transactional
	public User findByUserDetails(UserDetails userDetails) {
		if (userDetails == null){
			throw new IllegalStateException("User has to exists, but does not exist");
		}

		User user = this.findByUsername(userDetails.getUsername());
		if (user == null) {
			throw new IllegalStateException("User has to exists, but can't find in UserRepository");
		}

		return user;
	}
}
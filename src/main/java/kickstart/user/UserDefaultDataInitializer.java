package kickstart.user;

import java.time.LocalDate;
import java.util.List;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
@Order(10)
class UserDefaultDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(UserDefaultDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final UserManagement userManagement;

	UserDefaultDataInitializer(UserAccountManagement userAccountManagement, UserManagement userManagement) {
		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(userManagement, "UserRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.userManagement = userManagement;
	}

	@Override
	public void initialize() {

		//dont add new users if user already exist
		if (userAccountManagement.findByUsername("admin@example.com").isPresent()) {
			return;
		}

		LOG.info("Creating default admin, employees and users.");

		var password = "test";

		List.of(//
			new RegistrationForm("admin", password,password, "admins cave", "a", "s", "2022-4-4")
		).forEach(userManagement::createAdmin);

		List.of(//
			new RegistrationForm("employee1", password,password, "employees avenue 1", "a", "s", "2022-4-4"),
			new RegistrationForm("employee2@example.com", password,password, "employees avenue 2", "a", "s", "2022-4-4")
		).forEach(userManagement::createEmployee);


		List.of(//
			new RegistrationForm("test@example.com", password,password, "The test palace 34", "a", "s", "2022-4-4"),
			new RegistrationForm("user1@example.com", password,password, "Streetstreet 1", "a", "s", "2022-4-4"),
			new RegistrationForm("user2@example.com", password,password, "Streetstreet 2", "a", "s", "2022-4-4"),
			new RegistrationForm("user3@example.com", password,password, "Streetstreet 3", "a", "s", "2022-4-4")
			).forEach(userManagement::createCustomer);
	}
}

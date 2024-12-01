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

		List.of(
			new RegistrationForm("admin", password, password, "Hauptstraße 1, Bürogebäude", "Max", "Mustermann", "1980-01-15")
		).forEach(userManagement::createAdmin);

		List.of(
			new RegistrationForm("employee1", password, password, "Arbeiterweg 45", "Lisa", "Meier", "1992-05-24"),
			new RegistrationForm("employee2@example.com", password, password, "Fleißstraße 12", "Paul", "Schmidt", "1988-08-19"),
			new RegistrationForm("employee3@example.com", password, password, "Industriepark 9", "Clara", "Fischer", "1990-03-11")
		).forEach(userManagement::createEmployee);

		List.of(
			new RegistrationForm("customer", password, password, "Kundenallee 23", "Anna", "Weber", "1986-11-03"),
			new RegistrationForm("customer1@example.com", password, password, "Eichenweg 8", "Johannes", "Bauer", "1979-06-21"),
			new RegistrationForm("customer2@example.com", password, password, "Birkenstraße 14", "Sophie", "Klein", "1995-02-17"),
			new RegistrationForm("customer3@example.com", password, password, "Hauptplatz 3", "Martin", "Wolf", "1983-09-29"),
			new RegistrationForm("customer4@example.com", password, password, "Blumenweg 5", "Laura", "Hofmann", "1987-12-08")
		).forEach(userManagement::createCustomer);
	}
}

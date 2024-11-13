/*
q * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kickstart.user;

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
		if (userAccountManagement.findByUsername("admin").isPresent()) {
			return;
		}

		LOG.info("Creating default admin, employees and users.");

		var password = "test";

		List.of(//
			new RegistrationForm("admin", password,password, "admins cave")
		).forEach(userManagement::createAdmin);

		List.of(//
			new RegistrationForm("employee1", password,password, "employees avenue 1"),
			new RegistrationForm("employee2", password,password, "employees avenue 2")
		).forEach(userManagement::createEmployee);


		List.of(//
			new RegistrationForm("test", password,password, "The test palace 34"),
			new RegistrationForm("user1", password,password, "Streetstreet 1"),
			new RegistrationForm("user2", password,password, "Streetstreet 2"),
			new RegistrationForm("user3", password,password, "Streetstreet 3")
			).forEach(userManagement::createCustomer);
	}
}

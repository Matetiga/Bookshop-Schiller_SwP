package kickstart.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;


class UserManagementUnitTests {

	@Test 
	void createsUserAccountWhenCreatingACustomer() {

        // create UserRepository-Mock with action when save an entity then entity will returned 
		UserRepository repository = mock(UserRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// create UserAccountManager-Mock and UserAccount-Mock
        // UserAccountManager with action to return the userAccount which is created
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any(Role.class))).thenReturn(userAccount);

		// Usermanagement-Mock using UserRepository-Mock and userAccount-Mock
		UserManagement customerManagement = new UserManagement(repository, userAccountManager);

		// create RegistrationForm and submit
		RegistrationForm form = new RegistrationForm("name", "password", "password", "address");
		User customer = customerManagement.createCustomer(form);

		// so a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1))
				.create(eq(form.getUsername()), //
						eq(UnencryptedPassword.of(form.getPassword())), 
						eq(UserManagement.CUSTOMER_ROLE));

		// customer has a user account, which is not null
		assertThat(customer.getUserAccount()).isNotNull();
	}
}
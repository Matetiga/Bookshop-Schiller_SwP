package kickstart.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import kickstart.Achievement.Achievement;
import kickstart.orders.MyOrderManagement;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


class UserManagementUnitTests {

	@Test 
	void createsUserAccountWhenCreatingACustomer() {

        // create UserRepository-Mock with action when save an entity then entity will returned 
		UserRepository repository = mock(UserRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// create UserAccountManager-Mock and UserAccount-Mock
        // UserAccountManager with action to return the userAccount which is created
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		MyOrderManagement myOrderManagement = mock(MyOrderManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any(Role.class))).thenReturn(userAccount);

		// Usermanagement-Mock using UserRepository-Mock and userAccount-Mock
		UserManagement customerManagement = new UserManagement(repository, userAccountManager);

		// create RegistrationForm and submit
		RegistrationForm form = new RegistrationForm("john.doe@example.com", "password", "password", "address", "name", "last_name", "2022-2-2");
		User customer = customerManagement.createCustomer(form);

		// so a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1))
				.create(eq(form.getEmail()), //
						eq(UnencryptedPassword.of(form.getPassword())), 
						eq(UserManagement.CUSTOMER_ROLE));

		// customer has a user account, which is not null
		assertThat(customer.getUserAccount()).isNotNull();
	}
	
	@Test
    void createsCustomerSuccessfully() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
        UserAccount userAccount = mock(UserAccount.class);
        when(userAccountManager.create(any(), any(), eq(Role.of("CUSTOMER")))).thenReturn(userAccount);

        UserManagement userManagement = new UserManagement(repository, userAccountManager);

        RegistrationForm form = new RegistrationForm("john.doe@example.com", "password", "password", "address", "John", "Doe", "2000-01-01");
        User customer = userManagement.createCustomer(form);

        assertThat(customer).isNotNull();
        assertThat(customer.getUserAccount()).isEqualTo(userAccount);
        verify(userAccountManager).create(eq("john.doe@example.com"), eq(UnencryptedPassword.of("password")), eq(Role.of("CUSTOMER")));
        verify(repository).save(any(User.class));
    }

    

    @Test
    void throwsExceptionWhenPromotingNonExistentUser() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.findAll()).thenReturn(Streamable.empty());

        UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));
        UUID userId = UUID.randomUUID();

        assertThatThrownBy(() -> userManagement.promoteAccountById(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

	@Test
    void findsUserById() {
        UserRepository repository = mock(UserRepository.class);
        UserAccount userAccount = mock(UserAccount.class);
        User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");

        when(repository.findAll()).thenReturn(Streamable.of(user));

        UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));
		UUID id = UUID.randomUUID();
		UserAccountIdentifier userId = UserAccountIdentifier.of(id.toString());

        when(userAccount.getId()).thenReturn(userId);

        User foundUser = userManagement.findByID(id);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void promotesAccountSuccessfully() {
        UserRepository repository = mock(UserRepository.class);
        UserAccount userAccount = mock(UserAccount.class);
        User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");

        when(repository.findAll()).thenReturn(Streamable.of(user));
        when(userAccount.hasRole(Role.of("CUSTOMER"))).thenReturn(true);

        UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));
        UUID id = UUID.randomUUID();
		UserAccountIdentifier userId = UserAccountIdentifier.of(id.toString());
        when(userAccount.getId()).thenReturn(userId);

        String result = userManagement.promoteAccountById(id);

        assertThat(result).contains("Promotion successful");
        verify(userAccount).add(Role.of("EMPLOYEE"));
        verify(userAccount).remove(Role.of("CUSTOMER"));
    }


    @Test
    void findsUserByUserDetails() {
        UserRepository repository = mock(UserRepository.class);
        UserAccount userAccount = mock(UserAccount.class);
        User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");

        when(repository.findAll()).thenReturn(Streamable.of(user));
        when(userAccount.getUsername()).thenReturn("john.doe@example.com");

        UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("john.doe@example.com");

        User foundUser = userManagement.findByUserDetails(userDetails);

        assertThat(foundUser).isEqualTo(user);
    }

	@Test
	void findsUserByUsername() {
		UserRepository repository = mock(UserRepository.class);
		UserAccount userAccount = mock(UserAccount.class);
		User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");
	
		when(repository.findAll()).thenReturn(Streamable.of(user));
		when(userAccount.getUsername()).thenReturn("john.doe@example.com");
	
		UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));
	
		User foundUser = userManagement.findByUsername("john.doe@example.com");
	
		assertThat(foundUser).isEqualTo(user);
	}
	

	@Test
	void filtersCustomersByNameAndEmail() {
		UserRepository repository = mock(UserRepository.class);
		UserAccount userAccount1 = mock(UserAccount.class);
		User user1 = new User(userAccount1, "address1", "John", "Doe", "2000-01-01");

		UserAccount userAccount2 = mock(UserAccount.class);
		User user2 = new User(userAccount2, "address2", "Jane", "Smith", "1990-01-01");

		when(repository.findAll()).thenReturn(Streamable.of(user1, user2));
		when(user1.getEmail()).thenReturn("john.doe@example.com");

		UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));

		Set<User> filteredUsers = userManagement.filterCustomers(new HashSet<>(Arrays.asList(user1, user2)), "John", "john.doe@example.com");

		assertThat(filteredUsers).containsExactly(user1);
	}

	@Test
	void failsWhenUserNotFoundByID() {
		UserRepository repository = mock(UserRepository.class);
		when(repository.findAll()).thenReturn(Streamable.empty());

		UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));

		UUID id = UUID.randomUUID();

		// Test, dass null zurückgegeben wird, wenn kein Benutzer gefunden wird
		User user = userManagement.findByID(id);
		assertThat(user).isNull();
	}


	@Test
	void retrievesUserByIDSuccessfully() {
		UserRepository repository = mock(UserRepository.class);
		UserAccount userAccount = mock(UserAccount.class);
		User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");

		when(repository.findAll()).thenReturn(Streamable.of(user));

		UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));
		UUID id = UUID.randomUUID();
		UserAccountIdentifier userId = UserAccountIdentifier.of(id.toString());
		when(userAccount.getId()).thenReturn(userId);

		User foundUser = userManagement.findByID(id);

		assertThat(foundUser).isEqualTo(user);
	}

	@Test
	void findsUserByUsernameSuccessfully() {
		UserRepository repository = mock(UserRepository.class);
		UserAccount userAccount = mock(UserAccount.class);
		User user = new User(userAccount, "address", "John", "Doe", "2000-01-01");

		when(repository.findAll()).thenReturn(Streamable.of(user));
		when(userAccount.getUsername()).thenReturn("john.doe@example.com");

		UserManagement userManagement = new UserManagement(repository, mock(UserAccountManagement.class));

		User foundUser = userManagement.findByUsername("john.doe@example.com");

		assertThat(foundUser).isEqualTo(user);
	}
	
}
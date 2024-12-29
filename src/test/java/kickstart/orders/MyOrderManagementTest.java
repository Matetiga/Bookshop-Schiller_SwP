package kickstart.orders;

import kickstart.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyOrderManagementTest {
	private User mockUser;
	private UserAccount mockUserAccount;

	@BeforeEach
	void setUp() {
		mockUserAccount = mock(UserAccount.class);
		mockUser = mock(User.class);

		when(mockUser.getUserAccount()).thenReturn(mockUserAccount);
		when(mockUserAccount.getId()).thenReturn(UserAccount.UserAccountIdentifier.of("1"));
	}

	@Test
	public void testFindByUsername(){
		//List<MyOrder> list = new ArrayList<>();
		//MyOrder order = new MyOrder()
		//list.add()
	}
}

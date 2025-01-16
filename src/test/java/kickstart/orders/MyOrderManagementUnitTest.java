package kickstart.orders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import kickstart.user.User;
import kickstart.user.UserManagement;
import kickstart.Inventory.ShopProductCatalog;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyOrderManagementUnitTest {
	private User mockUser;
	private UserAccount mockUserAccount;
	private MyOrderRepository myOrderRepository;
	private UserManagement userManagement;
	private ShopProductCatalog shopProductCatalog;
	private MyOrderManagement myOrderManagement;

	@BeforeEach
	void setUp() {
		mockUserAccount = mock(UserAccount.class);
		mockUser = mock(User.class);
		myOrderRepository = mock(MyOrderRepository.class);
		userManagement = mock(UserManagement.class);
		shopProductCatalog = mock(ShopProductCatalog.class);
		myOrderManagement = new MyOrderManagement(myOrderRepository, userManagement, shopProductCatalog);

		when(mockUser.getUserAccount()).thenReturn(mockUserAccount);
		when(mockUserAccount.getId()).thenReturn(UserAccount.UserAccountIdentifier.of("1"));
		when(mockUserAccount.getUsername()).thenReturn("testUsername");

	}

	@Test
	void testFindByPaymentMethod() {
		MyOrder order1 = new MyOrder(mockUser, "Bar");
		MyOrder order2 = new MyOrder(mockUser, "Rechnung");
		MyOrder order3 = new MyOrder(mockUser, "Bar");

		List<MyOrder> orders = List.of(order1, order2, order3);

		Iterable<MyOrder> resultList = myOrderManagement.findByPaymentMethod("Bar", orders);

		List<MyOrder> filteredList = new ArrayList<>();
		resultList.forEach(filteredList::add);

		assertEquals(2, filteredList.size());
		assertTrue(filteredList.contains(order1));
		assertTrue(filteredList.contains(order3));
		assertFalse(filteredList.contains(order2));
	}

	@Test
	void testFindByStatus() {
		MyOrder order1 = new MyOrder(mockUser, "Bar");
		MyOrder order2 = new MyOrder(mockUser, "Rechnung");
		MyOrder order3 = new MyOrder(mockUser, "Bar");

		List<MyOrder> orders = List.of(order1, order2, order3);

		Iterable<MyOrder> resultList = myOrderManagement.findByStatus("Offen", orders);

		List<MyOrder> filteredList = new ArrayList<>();
		resultList.forEach(filteredList::add);

		assertEquals(3, filteredList.size());
		assertTrue(filteredList.contains(order1));
		assertTrue(filteredList.contains(order2));
		assertTrue(filteredList.contains(order3));
	}

	@Test
	void testFindByProductName() {
		MyOrder order1 = new MyOrder(mockUser, "Bar");
		MyOrder order2 = new MyOrder(mockUser, "Rechnung");
		MyOrder order3 = new MyOrder(mockUser, "Bar");

		HashSet<Genre> mockGenreSet = new HashSet<>();
		mockGenreSet.add(mock(Genre.class));

		order1.addOrderLine(new Book("MockBuch", "unwichtig", Money.of(9.99, "EUR"),
			"Nur noch ein Tag, dann ist morgen.", mockGenreSet, "R.Winkler",
			"42", "nein"), Quantity.of(1));

		List<MyOrder> orders = List.of(order1, order2, order3);

		Iterable<MyOrder> resultList = myOrderManagement.findByProductName("MockBuch", orders);

		List<MyOrder> filteredList = new ArrayList<>();
		resultList.forEach(filteredList::add);

		assertEquals(1, filteredList.size());
		assertTrue(filteredList.contains(order1));
		assertFalse(filteredList.contains(order3));
		assertFalse(filteredList.contains(order2));
	}

	@Test
	void testSortByDate() {
		MyOrder order1 = new MyOrder(mockUser, "Bar");
		order1.setDebitTime(LocalDateTime.now().plusSeconds(1));
		MyOrder order2 = new MyOrder(mockUser, "Rechnung");
		order2.setDebitTime(LocalDateTime.now().plusSeconds(2));
		MyOrder order3 = new MyOrder(mockUser, "Bar");
		order3.setDebitTime(LocalDateTime.now().plusSeconds(3));

		List<MyOrder> orders = List.of(order3, order2, order1);

		List<MyOrder> resultList = myOrderManagement.sortByDate(orders);

		assertEquals(order1, resultList.get(0));
		assertEquals(order2, resultList.get(1));
		assertEquals(order3, resultList.get(2));
	}
}


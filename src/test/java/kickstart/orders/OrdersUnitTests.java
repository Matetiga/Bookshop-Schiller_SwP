package kickstart.orders;

import kickstart.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.UserAccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrdersUnitTests {
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
	void testMyOrderRepository() {

		MyOrderRepository testRepository = mock(MyOrderRepository.class);
		when(testRepository.save(any())).then(i -> i.getArgument(0));
	}

	@Test
	void testCart() {
		Cart cart = mock(Cart.class);
		Product testProduct = mock(Product.class);
		long testAmount = 1;
		assertThat(cart.addOrUpdateItem(testProduct,testAmount).isPresent());
	}

	@Test
	void testMyOrderGetStringPaymentMethod() {
		String paymentMethod = "Rechnung";
		MyOrder order = new MyOrder(mockUser, paymentMethod);

		assertEquals(paymentMethod, order.getStringPaymentMethod());
	}

	@Test
	void testMyOrderDefaultConstructor() {
		MyOrder order = new MyOrder();

		assertNull(order.getStringPaymentMethod());
	}

	@Test
	void testChangeStatusRechnung() {
		MyOrder order = new MyOrder(mockUser, "Rechnung");

		order.changeStatus();
		assertEquals("in Lieferung", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("geliefert", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Abgeschlossen", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Offen", order.getMyOrderStatus());
	}

	@Test
	void testChangeStatusBar() {
		MyOrder order = new MyOrder(mockUser,"Bar");

		order.changeStatus();
		assertEquals("Abholbereit", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Abgeschlossen", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Offen", order.getMyOrderStatus());
	}
}

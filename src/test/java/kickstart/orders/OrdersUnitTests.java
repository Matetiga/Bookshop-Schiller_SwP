package kickstart.orders;

import kickstart.user.User;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrdersUnitTests {

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

	/*
	@Test
	void testMyOrderGetStringPaymentMethod() {
		User user = mock(User.class);

		String paymentMethod = "Rechnung";
		MyOrder order = new MyOrder(user, paymentMethod);

		assertEquals(paymentMethod, order.getStringPaymentMethod());
	}
	 */


	@Test
	void testMyOrderDefaultConstructor() {
		MyOrder order = new MyOrder();

		assertNull(order.getStringPaymentMethod());
	}

	/*
	@Test
	void testChangeStatusRechnung() {
		User user = new User(mock(UserAccount.class), mock(String.class), mock(String.class), mock(String.class), mock(String.class));

		MyOrder order = new MyOrder(user, "Rechnung");

		order.changeStatus();
		assertEquals("in Lieferung", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("geliefert", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Abgeschlossen", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Offen", order.getMyOrderStatus());
	}
	 */

	/*
	@Test
	void testChangeStatusBar() {
		User user = new User(mock(UserAccount.class), mock(String.class), mock(String.class), mock(String.class), mock(String.class));

		MyOrder order = new MyOrder(user,"Bar");

		order.changeStatus();
		assertEquals("Abholbereit", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Abgeschlossen", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Offen", order.getMyOrderStatus());
	}
	 */
}

package kickstart.orders;

import kickstart.orders.MyOrder;

import kickstart.orders.MyOrderRepository;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.UserAccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

	@Test
	void testMyOrderGetStringPaymentMethod() {
		UserAccount.UserAccountIdentifier userId = mock(UserAccount.UserAccountIdentifier.class);
		String paymentMethod = "CreditCard";
		MyOrder order = new MyOrder(userId, paymentMethod);

		assertEquals(paymentMethod, order.getStringPaymentMethod());
	}

	@Test
	void testMyOrderDefaultConstructor() {
		MyOrder order = new MyOrder();

		assertNull(order.getStringPaymentMethod());
	}

}

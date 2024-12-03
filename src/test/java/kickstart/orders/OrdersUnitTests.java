package kickstart.orders;

import kickstart.Inventory.Book;
import kickstart.orders.MyOrder;

import kickstart.orders.MyOrderRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Sort;

import static kickstart.Inventory.Genre.createGenre;
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

	@Test
	void testChangeStatusRechnung() {
		UserAccount.UserAccountIdentifier userId = mock(UserAccount.UserAccountIdentifier.class);
		MyOrder order = new MyOrder(userId, "Rechnung");

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
		UserAccount.UserAccountIdentifier userId = mock(UserAccount.UserAccountIdentifier.class);
		MyOrder order = new MyOrder(userId,"Bar");

		order.changeStatus();
		assertEquals("Abholbereit", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Abgeschlossen", order.getMyOrderStatus());

		order.changeStatus();
		assertEquals("Offen", order.getMyOrderStatus());
	}

}

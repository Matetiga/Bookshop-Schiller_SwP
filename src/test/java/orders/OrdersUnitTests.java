package orders;

import kickstart.orders.MyOrder;

import kickstart.orders.MyOrderRepository;
import kickstart.orders.OrderController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OrdersUnitTests {

	@Test
	void testingMyOrderRepository() {

		//OrderRepository returning orders handed into save(…)
		MyOrderRepository testRepository = mock(MyOrderRepository.class);
		when(testRepository.save(any())).then(i -> i.getArgument(0));
	}

	@Test
	void testingCart() {
		Cart cart = mock(Cart.class);
		Product testProduct = mock(Product.class);
		long testAmount = 1;
		assertThat(cart.addOrUpdateItem(testProduct,testAmount).isPresent());
	}

	@Test
	void testingMyOrder() {
		MyOrder myOrder = mock(MyOrder.class);
		Product testProduct = mock(Product.class);
		long testAmount = 42;
		myOrder.addOrderLine(testProduct, Quantity.of(testAmount));
		//assertThat(!(myOrder.getOrderLines().isEmpty()));
		//assertThat(myOrder.getStringPaymentMethod()).isNotNull();
	}
}

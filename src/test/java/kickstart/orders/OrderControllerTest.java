package kickstart.orders;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.Book;
import kickstart.Inventory.ShopProductCatalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest extends AbstractIntegrationTests {

	@Autowired private UniqueInventory<UniqueInventoryItem> shopProductInventory;
	@Autowired private OrderController orderController;
	@Autowired private ShopProductCatalog shopProductCatalog;

	private Model model = new ExtendedModelMap();

	OrderControllerTest(@Autowired UniqueInventory<UniqueInventoryItem> shopProductInventory,
						@Autowired OrderController orderController,
						@Autowired ShopProductCatalog shopProductCatalog){
		this.shopProductInventory = shopProductInventory;
		this.orderController = orderController;
		this.shopProductCatalog = shopProductCatalog;
	}

@Test
@WithMockUser(username = "admin", roles = "ADMIN")
public void testBuyWithEnoughStock(){
	Book book = (Book) shopProductCatalog.findByName("The Great Gatsby").stream().findFirst().get();

	UserDetails userDetails = mock(UserDetails.class);
	when(userDetails.getUsername()).thenReturn("admin");

	Cart cart = new Cart();
	cart.addOrUpdateItem(book, 4);

	// buy() method inside Order Controller must be tested
	orderController.buy(userDetails, cart, "Bar", model);

	shopProductInventory.findByProductIdentifier(book.getId()).ifPresent(item -> {
		assertEquals(Quantity.of(6), item.getQuantity());
	});

}

	@Test
	public void testNotEnoughStock() {
		Book book = (Book) shopProductCatalog.findByName("The Great Gatsby").stream().findFirst().get();

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getUsername()).thenReturn("admin");

		Cart cart = new Cart();
		cart.addOrUpdateItem(book, 123);

		// buy() method inside Order Controller must be tested
		orderController.buy(userDetails, cart, "Bar", model);

		shopProductInventory.findByProductIdentifier(book.getId()).ifPresent(item -> {
			assertEquals(Quantity.of(10), item.getQuantity());
		});

	}
}


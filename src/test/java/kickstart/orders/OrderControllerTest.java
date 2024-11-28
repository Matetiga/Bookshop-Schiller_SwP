package kickstart.orders;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.Book;
import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import kickstart.user.User;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import kickstart.user.UserManagement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.salespointframework.useraccount.UserAccount;

import java.util.UUID;

import static kickstart.Inventory.Genre.createGenre;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//TODO: This test is not complete
// it ALWAYS asserts true, even though the Stock Quantity is not correct
// it may be, because shopProductInventory is not being initialized, but only Mocked
// this may be the reason, why product's Quantity is not being updated
// ASK THE TUTOR!!!

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


package kickstart.orders;

import kickstart.Inventory.Book;
import kickstart.Inventory.ShopProduct;
import kickstart.user.User;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
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
import static org.mockito.Mockito.when;


//TODO: This test is not complete
// it ALWAYS asserts true, even though the Stock Quantity is not correct
// it may be, because shopProductInventory is not being initialized, but only Mocked
// this may be the reason, why product's Quantity is not being updated
// ASK THE TUTOR!!!

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

	@InjectMocks
	private OrderController orderController;

	@Mock
	private UserManagement userManagement;

	@Mock
	private UserDetails userDetails;

	@Mock
	private UserAccount userAccount;

	@Mock
	private User user;

	@Mock
	private Model model;

	@Mock
	private OrderLine orderLine;

	private UserAccount.UserAccountIdentifier userId;
	private Book exampleProduct1;

	@Mock
	private UniqueInventory<UniqueInventoryItem> shopProductInventory;

	private UniqueInventoryItem uniqueInventoryItem;

	@Mock
	private MyOrderRepository myOrderRepository;



	@BeforeEach
	void setUp() {
		// Setting up the system for the test
		Streamable<User> users = Streamable.of(user);
		when(userManagement.findAll()).thenReturn(users);

		when(user.getUserAccount()).thenReturn(userAccount);
		when(user.getUserAccount().getUsername()).thenReturn("admin");
		//when(userAccount.getUsername()).thenReturn("admin");
		when(userDetails.getUsername()).thenReturn("admin");

		userId = UserAccount.UserAccountIdentifier.of(UUID.randomUUID().toString());
		when(user.getUserAccount().getId()).thenReturn(userId);

		exampleProduct1 = new Book("Abarth", "gatsby.jpg", Money.of(10 ,"EUR"),
			"Mir gehts Abartig schlecht", createGenre("fiction"), "Jemand mit einem schönen Nachnamen",
			"9780743273565", "Scribner");

		uniqueInventoryItem = new UniqueInventoryItem(exampleProduct1, Quantity.of(10));
		shopProductInventory.save(uniqueInventoryItem);

		//TODO (inside deleteProductsFronStock() method)
		// inventory.findByProductIdentifier(orderLine.getProductIdentifier()).ifPresent(item -> {
		// there should be a when().thenReturn(), otherwise should throw an exception
		// but that exception is not being thrown

	}


	@Test
	void testBuyWithEnoughStock() {
		// Setting up the test

		Cart cart = new Cart();
		cart.addOrUpdateItem(exampleProduct1, 4);

		// buy() method inside Order Controller must be tested
		String viewName = orderController.buy(userDetails, cart, "credit_card", model);

		// test for item purchase with enough stock in inventory
		shopProductInventory.findByProductIdentifier(exampleProduct1.getId()).ifPresent(item -> {
			// TODO this should fail
			assertEquals(Quantity.of(645), item.getQuantity()); // Initial quantity (10) - purchased quantity (4) = 6
		});

	}

	@Test
	void testBuyNotEnoughStock(){
		Cart cart = new Cart();
		cart.addOrUpdateItem(exampleProduct1, 11);

		String viewName = orderController.buy(userDetails, cart, "credit_card", model);
		shopProductInventory.findByProductIdentifier(exampleProduct1.getId()).ifPresent(item -> {
			assertEquals(Quantity.of(104), item.getQuantity());
		});
	}

}


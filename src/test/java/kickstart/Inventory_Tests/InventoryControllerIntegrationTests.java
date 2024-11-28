package kickstart.Inventory_Tests;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.*;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.salespointframework.inventory.QInventoryItem.inventoryItem;

public class InventoryControllerIntegrationTests extends AbstractIntegrationTests {

	private Model model = new ExtendedModelMap();

	@Autowired
	InventoryController controller;

	@Autowired
	UniqueInventory<UniqueInventoryItem> shopProductInventory;

	@Autowired
	ShopProductCatalog shopProductCatalog;

	InventoryControllerIntegrationTests(@Autowired InventoryController controller,
										@Autowired UniqueInventory<UniqueInventoryItem> shopProductInventory,
										@Autowired ShopProductCatalog shopProductCatalog){
		this.controller = controller;
		this.shopProductInventory = shopProductInventory;
		this.shopProductCatalog = shopProductCatalog;
	}


	@Test
	public void testInventoryBookOverview() {
		String returnedView = controller.showInventory(model);

		Iterable<Object> books = (Iterable<Object>) model.asMap().get("books");
		assertThat(books).hasSize(4);
	}

	@Test
	public void testInventoryMerchOverview() {
		String returnedView = controller.showMerchInventory(model);

		Iterable<Object> merch = (Iterable<Object>) model.asMap().get("merch");
		assertThat(merch).hasSize(4);
	}

	@Test
	public void testInventoryCalenderOverview() {
		String returnedView = controller.showCalendarInventory(model);

		Iterable<Object> calenders = (Iterable<Object>) model.asMap().get("calendars");
		assertThat(calenders).hasSize(4);
	}

	@Test
	public void testInventoryDeleteProduct(){
		Book book = new Book("name", "im", Money.of(10, "EUR"),
			"des", Genre.createGenre("science Fiction"), "author", "ISBN", "publisher");

		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem(book, Quantity.of(10)));
		controller.deleteProduct(book.getId(), model);

		assertFalse(shopProductInventory.findByProductIdentifier(book.getId()).isPresent());

	}

//	//TODO: add test for increasing and decreasing quantity of product
//
//	@Test
//	public void testInventoryAddNewBook(){
//	}
//
//		AddBookForm form = new AddBookForm(" ", "img", "des", "science fiction",
//			"author", "ISBN", "publisher", 10.0, 10);
//		BindingResult bindingResult = new BeanPropertyBindingResult(form, "addBookForm");
//
//
//		ValidationUtils.invokeValidator(validator, form, bindingResult);
//
//		// Assert: Check for errors
//		assertThat(bindingResult.hasErrors()).isTrue();
//		assertThat(bindingResult.getFieldError("name")).isNotNull();
//		assertThat(bindingResult.getFieldError("name").getDefaultMessage()).isEqualTo("Name is required");
//
//	}
}

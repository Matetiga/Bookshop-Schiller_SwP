package kickstart.Inventory_Tests;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.*;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


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
		Iterable<Object> genres = (Iterable<Object>) model.asMap().get("bookGenres_addBook");

		assertThat(books).hasSize(4);
		assertThat(genres).hasSize(5);
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
	public void testIncreaseProductQuantity(){
		Book book = (Book) shopProductCatalog.findByName("Sapiens: A Brief History of Humankind").stream().findFirst().get();
		controller.increaseProductQuantity(book.getId(), "inventory_book", model);

		Calendar calendar = (Calendar) shopProductCatalog.findByName("Space Exploration 2024").stream().findFirst().get();
		controller.increaseProductQuantity(calendar.getId(), "inventory_calendar", model);

		Merch merch = (Merch) shopProductCatalog.findByName("Cap").stream().findFirst().get();
		controller.increaseProductQuantity(merch.getId(),"inventory_merch", model);

		assertThat(shopProductInventory.findByProductIdentifier(book.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
		assertThat(shopProductInventory.findByProductIdentifier(calendar.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
		assertThat(shopProductInventory.findByProductIdentifier(merch.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
	}

	@Test
	public void testDecreaseProductQuantity(){
		Book book = (Book) shopProductCatalog.findByName("The Great Gatsby").stream().findFirst().get();
		controller.decreaseProductQuantity(book.getId(), model);
//
		Calendar calendar = (Calendar) shopProductCatalog.findByName("Historical Monuments 2024").stream().findFirst().get();
		controller.decreaseProductQuantity(calendar.getId(),  model);
//
		Merch merch = (Merch) shopProductCatalog.findByName("Cap").stream().findFirst().get();
		controller.decreaseProductQuantity(merch.getId(), model);

		assertThat(shopProductInventory.findByProductIdentifier(book.getId()).get().getQuantity()).isEqualTo(Quantity.of(9));
		assertThat(shopProductInventory.findByProductIdentifier(calendar.getId()).get().getQuantity()).isEqualTo(Quantity.of(9));
		assertThat(shopProductInventory.findByProductIdentifier(merch.getId()).get().getQuantity()).isEqualTo(Quantity.of(9));
	}

	@Test
	public void testInventoryDeleteProduct(){
		Book book = new Book("name", "im", Money.of(10, "EUR"),
			"des", Genre.createGenre("science Fiction"), "author", "ISBN", "publisher");

		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem(book, Quantity.of(10)));
		controller.deleteProduct(book.getId(), model);

		Calendar calendar = (Calendar) shopProductCatalog.findByName("Nature 2024").stream().findFirst().get();
		controller.deleteProduct(calendar.getId(), model);

		Merch merch = (Merch) shopProductCatalog.findByName("T-Shirt").stream().findFirst().get();
		controller.deleteProduct(merch.getId(), model);

		assertFalse(shopProductInventory.findByProductIdentifier(book.getId()).isPresent());
		assertFalse(shopProductInventory.findByProductIdentifier(calendar.getId()).isPresent());
		assertFalse(shopProductInventory.findByProductIdentifier(merch.getId()).isPresent());

	}

	@Test
	public void testAddNewGenre(){
		controller.addNewGenre("newGenre", model);
		controller.addNewGenre("NewGenrE", model);

		Iterable<Object> genres = (Iterable<Object>) model.asMap().get("bookGenres_addBook");
		assertThat(genres).hasSize(6);

		boolean exists = false;
		for(Genre genre: Genre.getAllGenres()){
			if(genre.getGenre().equals("newGenre")){
				exists = true;
			}
		}
		assertTrue(exists);
	}

	@Test
	public void testAddNewGenreEmpty(){
		controller.addNewGenre("", model);

		Iterable<Object> genres = (Iterable<Object>) model.asMap().get("bookGenres_addBook");
		assertTrue(model.containsAttribute("error_newGenre"));
		assertThat(genres).hasSize(5);
	}

	@Test
	public void testDeleteGenre(){
		Genre.createGenre("testDeleteGenre");
		controller.deleteGenre("testDeleteGenre", model);

		Iterable<Object> genres = (Iterable<Object>) model.asMap().get("bookGenres_addBook");
		assertThat(genres).hasSize(5);

		boolean inexistent = false;
		for(Genre genre: Genre.getAllGenres()){
			if (genre.getGenre().equals("testDeleteGenre")){
				inexistent = true;
				break;
			}
		}
		assertFalse(inexistent);
	}
	//TODO this should test the Forms for the Book and the Calendar/Merch
	// but how to "activate" the "BindingResult"?
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

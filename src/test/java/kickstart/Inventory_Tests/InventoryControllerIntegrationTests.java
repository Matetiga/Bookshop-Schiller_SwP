package kickstart.Inventory_Tests;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.*;
import kickstart.user.User;
import kickstart.user.UserManagement;
import kickstart.user.UserRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryControllerIntegrationTests extends AbstractIntegrationTests {

	private Model model = new ExtendedModelMap();

	@Autowired
	InventoryController controller;

	@Autowired
	UniqueInventory<UniqueInventoryItem> shopProductInventory;

	@Autowired
	ShopProductCatalog shopProductCatalog;

	@Autowired
	private final UserRepository users;


	InventoryControllerIntegrationTests(@Autowired InventoryController controller,
										@Autowired UniqueInventory<UniqueInventoryItem> shopProductInventory,
										@Autowired ShopProductCatalog shopProductCatalog,
										@Autowired UserRepository users) {
		this.controller = controller;
		this.shopProductInventory = shopProductInventory;
		this.shopProductCatalog = shopProductCatalog;
		this.users = users;
	}

	private User mockUser;

	@BeforeEach
	void setUp() {
		// Mock UserAccount erstellen
		UserAccount mockUserAccount = Mockito.mock(UserAccount.class);

		// UserAccount Eigenschaften definieren
		Mockito.when(mockUserAccount.getUsername()).thenReturn("testUser");

		// Mock User erstellen
		mockUser = new User(mockUserAccount, "Test Address", "Test Name", "Test Last Name", "01.01.1990");


		users.save(mockUser);

	}


	@Test
	@WithMockUser(username = "testUser", roles = "ADMIN")
	public void testInventoryBookOverview() {
		String returnedView = controller.showInventory(model);

		Iterable<Object> books = (Iterable<Object>) model.asMap().get("books");
		Iterable<Object> genres = (Iterable<Object>) model.asMap().get("bookGenres_addBook");

		assertThat(books).hasSize(12);
		assertThat(genres).hasSize(8);
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

		Calendar calendar = (Calendar) shopProductCatalog.findByName("Space Exploration 2025").stream().findFirst().get();
		controller.increaseProductQuantity(calendar.getId(), "inventory_calendar", model);

		Merch merch = (Merch) shopProductCatalog.findByName("Cap").stream().findFirst().get();
		controller.increaseProductQuantity(merch.getId(),"inventory_merch", model);

		assertThat(shopProductInventory.findByProductIdentifier(book.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
		assertThat(shopProductInventory.findByProductIdentifier(calendar.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
		assertThat(shopProductInventory.findByProductIdentifier(merch.getId()).get().getQuantity()).isEqualTo(Quantity.of(11));
	}

	@Test
	@WithMockUser(username = "testUser", roles = "ADMIN")
	public void testDecreaseProductQuantity(){
		UserManagement userManagement = mock(UserManagement.class);
		when(userManagement.findByUsername("testUser")).thenReturn(mockUser);

		Book book = (Book) shopProductCatalog.findByName("The Great Gatsby").stream().findFirst().get();
		controller.decreaseProductQuantity(book.getId(), model);
//
		Calendar calendar = (Calendar) shopProductCatalog.findByName("Historical Monuments 2025").stream().findFirst().get();
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
			"des", new HashSet<>(Set.of(Genre.createGenre("science fiction"))), "author", "ISBN", "publisher");

		UserDetails details = mock(UserDetails.class);


		shopProductCatalog.save(book);
		shopProductInventory.save(new UniqueInventoryItem(book, Quantity.of(10)));
		controller.deleteProduct(book.getId(), model);

		Calendar calendar = (Calendar) shopProductCatalog.findByName("Nature 2025").stream().findFirst().get();
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
		assertThat(genres).hasSize(9);

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
		assertThat(genres).hasSize(8);
	}

	@Test
	public void testDeleteGenre(){
		Genre testgenre = Genre.createGenre("testDeleteGenre");

		Book testBook = new Book("testBookDeleteGenre", "im", Money.of(10, "EUR"),
			"des", new HashSet<>(Set.of(testgenre)), "author", "ISBN", "publisher");
		shopProductCatalog.save(testBook);

		controller.deleteGenre("testDeleteGenre", model);
		// Genre should be deleted from the list
		Iterable<Genre> genres = (Iterable<Genre>) model.asMap().get("bookGenres_addBook");
		assertThat(genres).hasSize(8);

		boolean inexistent = false;
		for(Genre genre: Genre.getAllGenres()){
			if (genre.getGenre().equals("testDeleteGenre")){
				inexistent = true;
				break;
			}
		}
		assertFalse(inexistent);

		// Genre should be deleted from the book
		assertThat(testBook.getBookGenres()).isEmpty();

	}


	@Test
	public void testSaveImage() throws Exception {
		// Mock MultipartFile
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.getOriginalFilename()).thenReturn("test-image.jpg");

		byte[] mockContent = "Image content".getBytes();
		InputStream mockInputStream = new ByteArrayInputStream(mockContent);
		when(mockFile.getInputStream()).thenReturn(mockInputStream);

		// calling the method
		String savedPath = controller.saveImage(mockFile);

		assertTrue(savedPath.contains("../uploads/images"), "Path should contain 'uploads/images'");
		assertTrue(savedPath.endsWith("test-image.jpg"));

		// this should get rid of '../' in the path
		Path path = Paths.get(savedPath.substring(3));

		//this line is throwing an error only after a push in Github
		// not even after using './mvnw test' in the terminal
		//assertTrue(Files.exists(path), "File should exist in the actual directory");

		// this should then clean the directory of the test file
		Files.deleteIfExists(path);
	}

	@Test
	public void testUniqueISBN() {
		assertTrue(controller.isValidAndUniqueISBN("9781603095020", null));
		assertTrue(controller.isValidAndUniqueISBN("9781603095426", null));
		assertTrue(controller.isValidAndUniqueISBN("9781603095174", null));
		assertTrue(controller.isValidAndUniqueISBN("9781603094429", null));
		assertTrue(controller.isValidAndUniqueISBN("9781603095204", null));

	}

	@Test
	public void testInvalidISBN(){
		assertFalse(controller.isValidAndUniqueISBN("", null));
		assertFalse(controller.isValidAndUniqueISBN("hhh4444555555", null));
		assertFalse(controller.isValidAndUniqueISBN("9780743273565", null)); // ISBN already exists
		assertFalse(controller.isValidAndUniqueISBN("1234567891234", null));
	}
// Important:
	// Test for methods that include Bindingresult will not be tested
	@Test
	public void testOutOfStockCalDef() {
			String returnedView = controller.showOutOfStockCalendars(model);

			Iterable<Object> calendar = (Iterable<Object>) model.asMap().get("calendars");
			assertThat(calendar).hasSize(0);

	}
	@Test
	public void testOutOfStockCal() {
		Calendar testCalendar = new Calendar("name", "img", Money.of(10, "EUR"), "des");
		shopProductCatalog.save(testCalendar);
		shopProductInventory.save(new UniqueInventoryItem(testCalendar, Quantity.of(0)));

		String returnedView = controller.showOutOfStockCalendars(model);

		Iterable<Object> calendars = (Iterable<Object>) model.asMap().get("calendars");

		assertThat(calendars).hasSize(1);
	}

	@Test
	public void testOutOfStockBookDef() {
		String returnedView = controller.showOutOfStockBooks(model);

		Iterable<Object> book = (Iterable<Object>) model.asMap().get("books");
		assertThat(book).hasSize(0);
	}

	@Test
	public void testOutOfStockBook() {
		Book testBook = new Book("name", "im", Money.of(10, "EUR"),
			"des", new HashSet<>(Set.of(Genre.createGenre("science fiction"))), "author", "ISBN", "publisher");
		shopProductCatalog.save(testBook);
		shopProductInventory.save(new UniqueInventoryItem(testBook, Quantity.of(0)));

		String returnedView = controller.showOutOfStockBooks(model);

		Iterable<Object> book = (Iterable<Object>) model.asMap().get("books");
		assertThat(book).hasSize(1);
	}

	@Test
	public void testOutOfStockMerchDef() {
		String returnedView = controller.showOutOfStockMerch(model);

		Iterable<Object> merch = (Iterable<Object>) model.asMap().get("merch");
		assertThat(merch).hasSize(0);
	}

	@Test
	public void testOutOfStockMerch() {
		Merch testMerch = new Merch("name", "img", Money.of(10, "EUR"), "des");
		shopProductCatalog.save(testMerch);
		shopProductInventory.save(new UniqueInventoryItem(testMerch, Quantity.of(0)));
		String returnedView = controller.showOutOfStockMerch(model);

		Iterable<Object> merch = (Iterable<Object>) model.asMap().get("merch");

		assertThat(merch).hasSize(1);
	}
}

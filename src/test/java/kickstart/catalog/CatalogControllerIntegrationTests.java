package kickstart.catalog;

import kickstart.AbstractIntegrationTests;

import kickstart.Inventory.Book;
import kickstart.Achievement.Achievement;
import kickstart.user.User;
import kickstart.user.UserManagement;
import kickstart.Inventory.Calendar;
import kickstart.Inventory.Merch;
import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.salespointframework.useraccount.Role;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.salespointframework.catalog.QProduct.product;

public class CatalogControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired CatalogController controller;
	@MockBean
	private UserManagement userManagement;
	private User mockUser;

	@Autowired
	ShopProductCatalog productCatalog;

	private Achievement ach1;

	@BeforeEach
	void setUp() {
		UserAccount mockUserAccount = Mockito.mock(UserAccount.class);
		Mockito.when(mockUserAccount.getUsername()).thenReturn("testUser");

		mockUser = new User(mockUserAccount, "Test Address", "Test Name", "Test Last Name", "01.01.1990");
		ach1 = new Achievement("Test Achievement", "Test Description", Role.of("ADMIN"));

		when(userManagement.findByUsername("testUser")).thenReturn(mockUser);
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	@SuppressWarnings("unchecked")
	public void bookCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.bookCatalog(model, null,"","","");

		assertThat(returnedView).isEqualTo("catalog_books");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(12);
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortBooksAscendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.bookCatalog(model, null, "asc", "", "");

		//assertThat(returnedView).isEqualTo("catalog_books");

		List<Book> books = (List<Book>) model.asMap().get("catalog");

		List<Double> prices = books.stream()
			.map(book -> book.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSorted();
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortBooksDescendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.bookCatalog(model, null, "desc", "", "");

		List<Book> books = (List<Book>) model.asMap().get("catalog");

		List<Double> prices = books.stream()
			.map(book -> book.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSortedAccordingTo((a,b) -> b.compareTo(a));
	}

//	@Test
//	@WithMockUser(username = "testUser", roles="ADMIN")
//	public void booksUnder10Test() {
//		Model model = new ExtendedModelMap();
//
//		String returnedView = controller.bookCatalog(model, null, "","under10", "");
//
//		List<Book> books = (List<Book>) model.asMap().get("catalog");
//
//		assertThat(books).allMatch(book -> book.getPrice().getNumber().doubleValue() < 10);
//
//	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	@SuppressWarnings("unchecked")
	public void merchCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.merchCatalog(model,"","","");

		assertThat(returnedView).isEqualTo("catalog_merch");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(4);
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortMerchsAscendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.merchCatalog(model, "asc", "", "");

		//assertThat(returnedView).isEqualTo("catalog_books");

		List<Merch> merches = (List<Merch>) model.asMap().get("catalog");

		List<Double> prices = merches.stream()
			.map(merch -> merch.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSorted();
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortMerchsDescendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.merchCatalog(model, "desc", "", "");

		List<Merch> merches = (List<Merch>) model.asMap().get("catalog");

		List<Double> prices = merches.stream()
			.map(merch -> merch.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSortedAccordingTo((a,b) -> b.compareTo(a));
	}

//	@Test
//	@WithMockUser(username = "testUser", roles="ADMIN")
//	public void merchUnder10Test() {
//		Model model = new ExtendedModelMap();
//
//		String returnedView = controller.merchCatalog(model, "","under10", "");
//
//		List<Merch> merches = (List<Merch>) model.asMap().get("catalog");
//
//		assertThat(merches).allMatch(merch -> merch.getPrice().getNumber().doubleValue() < 10);
//
//	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	@SuppressWarnings("unchecked")
	public void calenderCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.calenderCatalog(model,"","","");

		assertThat(returnedView).isEqualTo("catalog_calender");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(4);
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortCalendarsAscendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.calenderCatalog(model, "asc", "", "");

		//assertThat(returnedView).isEqualTo("catalog_books");

		List<Calendar> calendar = (List<Calendar>) model.asMap().get("catalog");

		List<Double> prices = calendar.stream()
			.map(calendars -> calendars.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSorted();
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	public void sortCalendarsDescendingTest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.calenderCatalog(model, "desc", "", "");

		List<Calendar> calendars = (List<Calendar>) model.asMap().get("catalog");

		List<Double> prices = calendars.stream()
			.map(calendar -> calendar.getPrice().getNumber().doubleValue())
			.collect(Collectors.toList());

		assertThat(prices).isSortedAccordingTo((a,b) -> b.compareTo(a));
	}


	@Test
	public void bookDetailControllerIntegrationTest() {
		ShopProduct bookProduct = productCatalog.findAll().stream()
			.filter(product -> product instanceof Book)
			.findFirst().orElseThrow(() -> new IllegalStateException("Kein Buch im Katalog gefunden"));

		Model model = new ExtendedModelMap();
		String returnedView = controller.bookDetail(bookProduct, model);

		assertThat(returnedView).isEqualTo("detail_book");
		assertThat(model.asMap().get("book")).isEqualTo(bookProduct);
		assertThat(model.asMap().get("quantity")).isInstanceOf(Quantity.class);
		assertThat(model.asMap().get("orderable")).isInstanceOf(Boolean.class);
	}

	@Test
	public void calendarDetailControllerIntegrationTest() {
		ShopProduct calendarProduct = productCatalog.findAll().stream()
			.filter(product -> product instanceof Calendar)
			.findFirst().orElseThrow(() -> new IllegalStateException("Kein Kalender im Katalog gefunden"));

		Model model = new ExtendedModelMap();
		String returnedView = controller.calenderDetail(calendarProduct, model);

		assertThat(returnedView).isEqualTo("detail_calender");
		assertThat(model.asMap().get("calender")).isEqualTo(calendarProduct);
		assertThat(model.asMap().get("quantity")).isInstanceOf(Quantity.class);
		assertThat(model.asMap().get("orderable")).isInstanceOf(Boolean.class);
	}

	@Test
	public void merchDetailControllerIntegrationTest() {
		ShopProduct merchProduct = productCatalog.findAll().stream()
			.filter(product -> product instanceof Merch)
			.findFirst().orElseThrow(() -> new IllegalStateException("Kein Merch im Katalog gefunden"));

		Model model = new ExtendedModelMap();
		String returnedView = controller.merchDetail(merchProduct,model);

		assertThat(returnedView).isEqualTo("detail_merch");
		assertThat(model.asMap().get("merch")).isEqualTo(merchProduct);
		assertThat(model.asMap().get("quantity")).isInstanceOf(Quantity.class);
		assertThat(model.asMap().get("orderable")).isInstanceOf(Boolean.class);
	}
}

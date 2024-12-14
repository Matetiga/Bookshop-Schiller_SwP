package kickstart.catalog;

import kickstart.AbstractIntegrationTests;

import kickstart.Inventory.Book;
import kickstart.Inventory.Calendar;
import kickstart.Inventory.Merch;
import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.catalog.QProduct.product;

public class CatalogControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired CatalogController controller;

	@Autowired
	ShopProductCatalog productCatalog;

	@Test
	@SuppressWarnings("unchecked")
	public void bookCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.bookCatalog(model, null);

		assertThat(returnedView).isEqualTo("catalog_books");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(12);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void merchCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.merchCatalog(model);

		assertThat(returnedView).isEqualTo("catalog_merch");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(4);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void calenderCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.calenderCatalog(model);

		assertThat(returnedView).isEqualTo("catalog_calender");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(4);
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

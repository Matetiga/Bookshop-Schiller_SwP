package kickstart.catalog;

import kickstart.AbstractIntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;

public class CatalogControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired CatalogController controller;

	@Test
	@SuppressWarnings("unchecked")
	public void bookCatalogControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.bookCatalog(model, null);

		assertThat(returnedView).isEqualTo("catalog_books");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");

		assertThat(object).hasSize(4);
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
}

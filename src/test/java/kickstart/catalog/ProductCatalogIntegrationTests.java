package kickstart.catalog;

import kickstart.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCatalogIntegrationTests extends AbstractIntegrationTests {
	@Autowired ProductCatalog catalog;

	@Test
	void findsAllBooks() {
		Iterable<ShopProduct> result = catalog.findByType(ShopProduct.ProductType.BOOK);
		assertThat(result).hasSize(12);
	}

	@Test
	void findsAllMerch() {
		Iterable<ShopProduct> result = catalog.findByType(ShopProduct.ProductType.MERCH);
		assertThat(result).hasSize(2);
	}

	@Test
	void findsAllCalenders() {
		Iterable<ShopProduct> result = catalog.findByType(ShopProduct.ProductType.CALENDER);
		assertThat(result).hasSize(4);
	}
}

package kickstart.catalog;

import static org.salespointframework.core.Currencies.*;

import kickstart.catalog.ShopProduct.ProductType;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
public class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private final ProductCatalog productCatalog;

	public CatalogDataInitializer(ProductCatalog productCatalog) {

		Assert.notNull(productCatalog, "ProductCatalog must not be null!");

		this.productCatalog = productCatalog;
	}

	@Override
	public void initialize() {
		if (productCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default catalog entries.");

		productCatalog.save(new ShopProduct(
			"Eine Kurze Reise durch die Zeit - Stephen Hawking",
			"stephen",
			Money.of(10, EURO),
			"Populärwissenschaft",
			ProductType.BOOK
			));

		productCatalog.save(new ShopProduct(
			"Codeknacker gegen Codemacher - Klaus Schmeh",
			"stephen",
			Money.of(15, EURO),
			"Geschichte",
			ProductType.BOOK
		));
	}
}

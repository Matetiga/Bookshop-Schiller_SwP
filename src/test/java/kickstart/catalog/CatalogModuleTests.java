package kickstart.catalog;

import kickstart.Inventory.ShopProductCatalog;
import org.junit.jupiter.api.Test;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
public class CatalogModuleTests {
	@Autowired
	ShopProductCatalog catalog;
	@Autowired ConfigurableApplicationContext context;

	@Test
		/*
		 * Dieser Test stellt sicher, dass das Catalog-Modul korrekt gestartet wird, ohne die Accountancy-Bean zu laden.
		 * Dadurch wird bestätigt, dass das Catalog-Modul keine unerwünschte Abhängigkeit zu Accountancy hat und sauber isoliert ist.
		 * */
	void verifiesModuleBootstrapped() {

		AssertableApplicationContext assertable = AssertableApplicationContext.get(() -> context);

		assertThat(assertable).doesNotHaveBean(Accountancy.class);
	}
}

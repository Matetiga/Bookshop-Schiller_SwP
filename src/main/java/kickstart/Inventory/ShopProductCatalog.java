package kickstart.Inventory;

import kickstart.Inventory.Products.ShopProduct;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface ShopProductCatalog extends Catalog<ShopProduct> {
	static final Sort DEFAULT_SORT = Sort.sort(ShopProduct.class).by(ShopProduct::getId).descending();

}

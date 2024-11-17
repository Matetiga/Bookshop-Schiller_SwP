package kickstart.catalog;

import kickstart.catalog.ShopProduct.ProductType;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface ProductCatalog extends Catalog<ShopProduct> {

	static final Sort DEFAULT_SORT = Sort.sort(ShopProduct.class).by(ShopProduct::getId).descending();

	Streamable<ShopProduct> findByType(ProductType type, Sort sort);

	default Streamable<ShopProduct> findByType(ProductType type) {
		return findByType(type, DEFAULT_SORT);
	}
}

package kickstart.Inventory;

import kickstart.Inventory.Products.Calendar;
import kickstart.Inventory.Products.Merch;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface MerchCatalog extends Catalog<Merch> {
	static final Sort DEFAULT_SORT = Sort.sort(Merch.class).by(Merch::getId).descending();

}

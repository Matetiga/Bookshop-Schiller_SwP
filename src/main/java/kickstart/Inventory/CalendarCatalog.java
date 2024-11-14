package kickstart.Inventory;

import kickstart.Inventory.Products.Calendar;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface CalendarCatalog extends Catalog<Calendar> {
	static final Sort DEFAULT_SORT = Sort.sort(Calendar.class).by(Calendar::getId).descending();
}


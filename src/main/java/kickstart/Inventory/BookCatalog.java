package kickstart.Inventory;

import kickstart.Inventory.Products.Book;
import kickstart.Inventory.Products.ShopProduct;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface BookCatalog extends Catalog<ShopProduct> {

	static final Sort DEFAULT_SORT = Sort.sort(Book.class).by(Book::getId).descending();

}

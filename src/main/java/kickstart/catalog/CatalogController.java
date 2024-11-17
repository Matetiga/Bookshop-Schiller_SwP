package kickstart.catalog;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CatalogController {
	private static final Quantity NONE = Quantity.of(0);

	private final ProductCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final BusinessTime businessTime;

	CatalogController(ProductCatalog productCatalog, UniqueInventory<UniqueInventoryItem> inventory,
					  BusinessTime businessTime) {

		this.catalog = productCatalog;
		this.inventory = inventory;
		this.businessTime = businessTime;
	}

	@GetMapping("/books")
	String bookCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(ShopProduct.ProductType.BOOK));
		model.addAttribute("title", "catalog.book.title");

		return "catalog_books";
	}

	@GetMapping("/merch")
	String merchCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(ShopProduct.ProductType.MERCH));
		model.addAttribute("title", "catalog.merch.title");

		return "catalog_merch";
	}

	@GetMapping("/calenders")
	String calenderCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(ShopProduct.ProductType.CALENDER));
		model.addAttribute("title", "catalog.calender.title");

		return "catalog_calender";
	}

	@GetMapping("/book/{bookProduct}")
	String bookDetail(@PathVariable ShopProduct bookProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(bookProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("book", bookProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_book";
	}

	@GetMapping("/merch/{merchProduct}")
	String merchDetail(@PathVariable ShopProduct merchProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(merchProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("merch", merchProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_merch";
	}

	@GetMapping("/calender/{calenderProduct}")
	String calenderDetail(@PathVariable ShopProduct calenderProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(calenderProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("calender", calenderProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail_calender";
	}


}

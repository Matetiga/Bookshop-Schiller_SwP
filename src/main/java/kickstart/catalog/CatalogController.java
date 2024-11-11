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

		return "catalog";
	}

	@GetMapping("/books/{book}")
	String detail(@PathVariable ShopProduct bookProduct, Model model) {

		var quantity = inventory.findByProductIdentifier(bookProduct.getId()) //
			.map(InventoryItem::getQuantity) //
			.orElse(NONE);

		model.addAttribute("book", bookProduct);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail";
	}
}

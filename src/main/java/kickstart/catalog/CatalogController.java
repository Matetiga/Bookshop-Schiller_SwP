package kickstart.catalog;
import kickstart.Inventory.Book;
import kickstart.Inventory.Calendar;
import kickstart.Inventory.Merch;
import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.ShopProductCatalog;
import kickstart.orders.OrderController;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CatalogController {
	private static final Quantity NONE = Quantity.of(0);

	private final ShopProductCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;


	CatalogController(ShopProductCatalog productCatalog, UniqueInventory<UniqueInventoryItem> inventory) {

		this.catalog = productCatalog;
		this.inventory = inventory;
	}

	@GetMapping("/books")
	String bookCatalog(Model model, @RequestParam(value = "genres", required = false) List<String> selectedGenres) {

		List<Book> allBooks = inventory.findAll().stream()
			.map(UniqueInventoryItem::getProduct)
			.filter(product -> product instanceof Book)
			.map(product -> (Book) product)
			.toList();

		Set<String> allGenres = allBooks.stream()
			.map(Book::getBookGenre)
			.collect(Collectors.toSet());

		List<Book> filteredBooks = selectedGenres == null || selectedGenres.isEmpty()
			? allBooks
			: allBooks.stream()
			.filter(book -> selectedGenres.contains(book.getBookGenre()))
			.toList();

		model.addAttribute("catalog", filteredBooks);
		model.addAttribute("genres", allGenres);
		model.addAttribute("selectedGenres", selectedGenres);
		model.addAttribute("title", "catalog.book.title");

		return "catalog_books";
	}

	@GetMapping("/merch")
	String merchCatalog(Model model) {

		List<Merch> catalog = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Merch){
				catalog.add((Merch) item.getProduct());
			}
		}

		model.addAttribute("catalog", catalog);
		model.addAttribute("title", "catalog.merch.title");

		return "catalog_merch";
	}

	@GetMapping("/calenders")
	String calenderCatalog(Model model) {

		List<Calendar> catalog = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Calendar){
				catalog.add((Calendar) item.getProduct());
			}
		}

		model.addAttribute("catalog", catalog);
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

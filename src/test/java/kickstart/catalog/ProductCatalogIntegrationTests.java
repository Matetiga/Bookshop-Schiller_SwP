package kickstart.catalog;

import kickstart.AbstractIntegrationTests;
import kickstart.Inventory.Products.*;
import kickstart.Inventory.ShopProductCatalog;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCatalogIntegrationTests extends AbstractIntegrationTests {
	@Autowired
	ShopProductCatalog catalog;
	UniqueInventory<UniqueInventoryItem> inventory;
	ProductCatalogIntegrationTests(@Autowired UniqueInventory<UniqueInventoryItem> inventory, @Autowired ShopProductCatalog catalog){
		this.inventory = inventory;
		this.catalog = catalog;
	}

	@Test
	void findsAllBooks() {
		List<Book> result = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Book){
				result.add((Book) item.getProduct());
			}
		}
		assertThat(result).hasSize(4);
	}

	@Test
	void findsAllMerch() {
		List<Merch> result = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Merch){
				result.add((Merch) item.getProduct());
			}
		}
		assertThat(result).hasSize(4);
	}

	@Test
	void findsAllCalenders() {
		List<Calendar> result = new ArrayList<>();
		for(UniqueInventoryItem item : inventory.findAll()){
			if(item.getProduct() instanceof Calendar){
				result.add((Calendar) item.getProduct());
			}
		}
		assertThat(result).hasSize(4);
	}
}

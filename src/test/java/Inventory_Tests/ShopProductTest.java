package Inventory_Tests;

import kickstart.Inventory.Products.Genre;
import kickstart.Inventory.Products.ShopProduct;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopProductTest {

	private ShopProduct product;

	@BeforeEach
	public void setUp() {

		Genre genre = new Genre("Science Fiction");

		this.product = new ShopProduct("Test", "imageURL",
			Money.of(10, "EUR"), 1, "description");
	}

	@Test
	public void testShopProductConstructor() {

		// Test for a valid parameters
		Assertions.assertEquals("Test", product.getName());
		Assertions.assertEquals("imageURL", product.getImage());
		Assertions.assertEquals(Money.of(10, "EUR"), product.getPrice());
		Assertions.assertEquals(1, product.getProductId());

		// Test for a invalid parameters
		// TODO Figure out a Test for the ID, there should not be two IDs with the same value
		// No tests for name and Price as they are tested in the Product class
		try {
			new ShopProduct("Title", "", Money.of(10, "EUR"), 1, "descritpion");
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals("ShopProduct image cannot be blank", e.getMessage());
		}

		try {
			new ShopProduct("Title", "null", Money.of(10, "EUR"), 1, "descritpion");
		} catch(NullPointerException e){
			Assertions.assertEquals("ShopProduct Image cannot be null", e.getMessage());
		}

		try{
			new ShopProduct("Title", "imageURL", Money.of(10, "EUR"), 1, "");
		} catch(IllegalArgumentException e){
			Assertions.assertEquals("ShopProduct Description cannot be blank", e.getMessage());
		}

		try{
			new ShopProduct("Title", "imageURL", Money.of(10, "EUR"), 1, null);
		} catch(NullPointerException e){
			Assertions.assertEquals("ShopProduct Description cannot be null", e.getMessage());
		}
	}


	@Test
	public void testShopProductSetImage() {
		product.setImage("New Image");
		Assertions.assertEquals("New Image", product.getImage());

		try{
			product.setImage("");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("ShopProduct Image cannot be blank", e.getMessage());
		}

		try{
			product.setImage(null);
		}catch (NullPointerException e){
		Assertions.assertEquals("ShopProduct Image cannot be null", e.getMessage());
		}
	}

	@Test
	public void testShopProductSetDescription() {
		product.setDescription("New Description");
		Assertions.assertEquals("New Description", product.getDescription());

		try{
			product.setDescription("");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("ShopProduct Description cannot be blank", e.getMessage());
		}

		try{
			product.setDescription(null);
		}catch (NullPointerException e){
			Assertions.assertEquals("ShopProduct Description cannot be null", e.getMessage());
		}
	}

	// TODO Test for the ID
}


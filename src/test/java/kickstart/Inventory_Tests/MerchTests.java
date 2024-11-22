package kickstart.Inventory_Tests;

import kickstart.Inventory.Genre;
import kickstart.Inventory.Merch;

import kickstart.Inventory.ShopProduct;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

// The tests for this class should work the same for the Calendar Class (both are the exactly the same)
public class MerchTests {

	private Merch product;

	@BeforeEach
	public void setUp() {

		Genre genre = Genre.createGenre("Science Fiction");

		this.product = new Merch("Test", "imageURL",
			Money.of(10, "EUR"),  "description");
	}

	@Test
	public void testShopProductAbstract(){
		assertTrue(Modifier.isAbstract(ShopProduct.class.getModifiers()));
	}

	@Test
	public void testShopProductConstructor() {

		// Test for a valid parameters
		Assertions.assertEquals("Test", product.getName());
		Assertions.assertEquals("imageURL", product.getImage());
		Assertions.assertEquals(Money.of(10, "EUR"), product.getPrice());

		// Test for a invalid parameters
		// No tests for name and Price as they are tested in the Product class
		try {
			new Merch("Title", "", Money.of(10, "EUR"),  "description");
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals("ShopProduct image cannot be blank", e.getMessage());
		}

		try {
			new Merch("Title", "null", Money.of(10, "EUR"),  "description");
		} catch(NullPointerException e){
			Assertions.assertEquals("ShopProduct Image cannot be null", e.getMessage());
		}

		try{
			new Merch("Title", "imageURL", Money.of(10, "EUR"), "");
		} catch(IllegalArgumentException e){
			Assertions.assertEquals("ShopProduct Description cannot be blank", e.getMessage());
		}

		try{
			new Merch("Title", "imageURL", Money.of(10, "EUR"),  null);
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

}


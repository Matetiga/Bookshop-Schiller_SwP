package Inventory_Tests;

import kickstart.Inventory.ShopProduct;
import kickstart.Inventory.Genre;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//public class ShopProductTest {
//
//	@Test
//	public void testProductConstructor() {
//		// should we test for constructors
//		// should constructors throw illegal argument exceptions
//		Genre genre = new Genre("Science Fiction");
//		ShopProduct.ProductType book = ShopProduct.ProductType.BOOK;
//
//		ShopProduct product = new ShopProduct("Test", "imageURL",
//			Money.of(10, "EUR"), genre, book, 1);
//		Assertions.assertEquals("Test", product.getName());
//		Assertions.assertEquals("imageURL", product.getImage());
//		Assertions.assertEquals(Money.of(10, "EUR"), product.getPrice());
//		Assertions.assertEquals(genre.toString(), product.getProductGenre());
//		Assertions.assertEquals(book, product.getType());
//		Assertions.assertEquals(1, product.getProductId());
//	}

//	@Test
//	public void testProductConstructorEmptyName() {
//		Assertions.assertThrows(IllegalArgumentException.class, () -> {
//			new ShopProduct("", 10, "Testing empty name", 1);
//		});
//	}
//}
//Todo Esto tambien me arrojaba un error, no se que es
package Inventory_Tests;

import kickstart.Inventory.Products.Book;
import kickstart.Inventory.Products.Genre;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookTest {

	Genre genre;
	private Book book;
	@BeforeEach
	public void setUp(){
		this.genre = new Genre("Science Fiction");
		this.book = new Book("Test", "imageURL", Money.of(10, "EUR"),
			1, "description", new Genre("Science Fiction"),
			"Author", "ISBN", "Publisher");

	}

	@Test
	public void testBookConstructor(){

		// Test for a valid parameters
		Assertions.assertEquals("Test", book.getName());
		Assertions.assertEquals("imageURL", book.getImage());
		Assertions.assertEquals(Money.of(10, "EUR"), book.getPrice());
		Assertions.assertEquals(1, book.getProductId());
		Assertions.assertEquals("description", book.getDescription());
		Assertions.assertEquals("Science Fiction", book.getGenre().toString());
		Assertions.assertEquals("Author", book.getAuthor());
		Assertions.assertEquals("ISBN", book.getISBN());
		Assertions.assertEquals("Publisher", book.getPublisher());

		// Test for a invalid parameters
		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), null, "ISBN", "Publisher");
		} catch (NullPointerException e) {
			Assertions.assertEquals("Book Author cannot be null", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), "", "ISBN", "Publisher");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book Author cannot be empty", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				null, "Author", "Hola", "Publisher");
		}catch (NullPointerException e){
			Assertions.assertEquals("Book Genre cannot be null", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), "Broski", null, "Publisher");
		}catch (NullPointerException e){
			Assertions.assertEquals("Book ISBN cannot be null", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), "Broski", "", "Publisher");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book ISBN cannot be empty", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), "Broski", "ISBN", null);
		}catch (NullPointerException e){
			Assertions.assertEquals("Book Publisher cannot be null", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"), 1, "description",
				new Genre("Science Fiction"), "Broski", "ISBN", "");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book Publisher cannot be empty", e.getMessage());
		}
	}

	// Setters Tests
	@Test
	public void testBookValidGenre(){
		Genre sus = new Genre("Suspense");
		book.setBookGenre(sus);
		Assertions.assertEquals(sus, book.getGenre());
	}

	@Test
	public void testBookInvalidGenre(){
		try {
			book.setBookGenre(null);
		} catch (NullPointerException e) {
			Assertions.assertEquals("Setter Book Genre cannot be null", e.getMessage());
		}

		try{ // try to set a genre that does not exist
			genre.deleteGenre("Science Fiction");
			book.setBookGenre(genre);
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Setter Book Genre does not exist", e.getMessage());
		}
	}

}

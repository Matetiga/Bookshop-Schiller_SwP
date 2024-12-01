package kickstart.Inventory_Tests;

import kickstart.Inventory.Book;
import kickstart.Inventory.Genre;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static kickstart.Inventory.Genre.createGenre;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {

	Genre genre;
	private Book book;
	private Set<Genre> genres = new HashSet<>();

	@BeforeEach
	public void setUp(){
		this.genre = createGenre("Science Fiction");
		genres.add(genre);
		this.book = new Book("Test", "imageURL", Money.of(10, "EUR"),
			"description", genres,
			"Author", "ISBN", "Publisher");

	}

	@Test
	public void testBookConstructor(){

		// Test for a valid parameters
		Assertions.assertEquals("Test", book.getName());
		Assertions.assertEquals("imageURL", book.getImage());
		Assertions.assertEquals(Money.of(10, "EUR"), book.getPrice());
		Assertions.assertEquals("description", book.getDescription());
		Assertions.assertEquals("Author", book.getAuthor());
		Assertions.assertEquals("ISBN", book.getISBN());
		Assertions.assertEquals("Publisher", book.getPublisher());
		assertThat(book.getBookGenres()).contains(genre);

		// Test for a invalid parameters
		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"), "description",
				genres, null, "ISBN", "Publisher");
		} catch (NullPointerException e) {
			Assertions.assertEquals("Book Author cannot be null", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				genres, "", "ISBN", "Publisher");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book Author cannot be empty", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				Collections.emptySet(), "Author", "Hola", "Publisher");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book Genre cannot be empty", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				genres, "Broski", null, "Publisher");
		}catch (NullPointerException e){
			Assertions.assertEquals("Book ISBN cannot be null", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				genres, "Broski", "", "Publisher");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book ISBN cannot be empty", e.getMessage());
		}

		try{
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				genres, "Broski", "ISBN", null);
		}catch (NullPointerException e){
			Assertions.assertEquals("Book Publisher cannot be null", e.getMessage());
		}

		try {
			new Book("Title", "imageURL", Money.of(10, "EUR"),  "description",
				genres, "Broski", "ISBN", "");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Book Publisher cannot be empty", e.getMessage());
		}
	}

	// Setters Tests
	@Test
	public void testBookValidGenre(){
		Genre sus = createGenre("Cooking");
		book.addBookGenre(sus);
		assertThat(book.getBookGenres()).contains(sus);
	}

	@Test
	public void testBookInvalidGenre() {
		assertThrows(NullPointerException.class, () -> book.addBookGenre(null));

		// try to add a genre that is already added
		assertThrows(IllegalArgumentException.class, () -> book.addBookGenre(genre));

		Genre.deleteGenre(genre);
		// try to add a genre that does not exist
		assertThrows(IllegalArgumentException.class, () -> book.addBookGenre(genre));

	}

	@Test
	public void testBookDeleteGenre(){
		Genre sus = createGenre("Cooking");
		book.addBookGenre(sus);
		assertThat(book.getBookGenres()).contains(sus);
		book.deleteBookGenre(sus);
		assertThat(book.getBookGenres()).doesNotContain(sus);
	}

	@Test
	public void testBookDeleteInvalidGenre(){
		assertThrows(NullPointerException.class, () -> book.deleteBookGenre(null));

		Genre test = createGenre("testDelete");
		book.addBookGenre(test);
		book.deleteBookGenre(test);
		assertThrows(IllegalArgumentException.class, () -> book.deleteBookGenre(test));

		Genre.deleteGenre(test);
		assertThrows(IllegalArgumentException.class, () -> book.deleteBookGenre(test));
	}

	@Test
	public void testBookSetAuthor(){
		book.setAuthor("New Author");
		Assertions.assertEquals("New Author", book.getAuthor());
		try{
			book.setAuthor("");
		}catch (IllegalArgumentException e){
			Assertions.assertEquals("Setter Book Author cannot be empty", e.getMessage());
		}
		try{
			book.setAuthor(null);
		}catch (NullPointerException e){
			Assertions.assertEquals("Setter Book Author cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBookSetISBN(){
		book.setISBN("New ISBN");
		Assertions.assertEquals("New ISBN", book.getISBN());
		assertThrows(IllegalArgumentException.class, () -> book.setISBN(""));
	 	assertThrows(NullPointerException.class, () -> book.setISBN(null));
	}

	@Test
	public void testBookSetPublisher(){
		book.setPublisher("New Publisher");
		Assertions.assertEquals("New Publisher", book.getPublisher());
		assertThrows(IllegalArgumentException.class, () -> book.setPublisher(""));
		assertThrows(NullPointerException.class, () -> book.setPublisher(null));
	}
}



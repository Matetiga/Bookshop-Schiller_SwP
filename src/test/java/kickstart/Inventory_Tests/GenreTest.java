package kickstart.Inventory_Tests;

import kickstart.Inventory.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static kickstart.Inventory.Genre.deleteGenre;
import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

	@BeforeEach
	public void clearGenreList(){
		Genre.getAllGenres().clear();
	}

	@Test
	public void testCreateGenre() {
		Assertions.assertEquals(0, Genre.getAllGenres().size());

		Genre genre = Genre.createGenre("Science Fiction");
		Assertions.assertEquals( 1, Genre.getAllGenres().size());
		assertTrue(Genre.getAllGenres().contains(genre), "Genre not added to the list");

		Genre genre2 = Genre.createGenre("Cooking");
		Genre genre3 = Genre.createGenre("Drama");
		Assertions.assertEquals(3, Genre.getAllGenres().size());
		assertTrue(Genre.getAllGenres().contains(genre2), "Genre not added to the list");
		assertTrue(Genre.getAllGenres().contains(genre3), "Genre not added to the list");
	}

	@Test
	public void testInvalidGenre(){
		assertThrows(IllegalArgumentException.class, () -> Genre.createGenre(""));
		assertThrows(NullPointerException.class, () -> Genre.createGenre(null));
	}

	@Test
	public void testCreateDuplicateGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("Science Fiction");
		Assertions.assertEquals(1, Genre.getAllGenres().size());
		assertTrue(Genre.getAllGenres().contains(genre2), "Genre not added to the list");
	}

	@Test
	public void testCreateLowerUppercaseDuplicateGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("sCieNceFiction");

		Assertions.assertEquals(1, Genre.getAllGenres().size());
	}

	@Test
	public void testDeleteGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("Fantasy");
		Genre genre3 = Genre.createGenre("Horror");
		Assertions.assertEquals(3, Genre.getAllGenres().size(), "Genres not added to the list");

		deleteGenre(genre);

		Assertions.assertEquals(2, Genre.getAllGenres().size());
		assertFalse(Genre.getAllGenres().contains(genre), "Genre not removed from the list");

	}

	@Test
	public void testDeleteNonExistingGenre(){
		assertThrows(NullPointerException.class, () -> deleteGenre(null));
		Genre genre = Genre.createGenre("Science Fiction");
		deleteGenre(genre);
		assertThrows(IllegalArgumentException.class, () -> deleteGenre(genre));
	}

	@Test
	public void testEqualGenres(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("scIence fiCtion");
		Genre genre3 = Genre.createGenre("Fantasy");
		assertTrue(genre.equals(genre2), "Genres are not equal");
		assertFalse(genre.equals(genre3), "Genres are equal");
		assertFalse(genre.equals("Science Fiction"), "Genres are equal");
	}

}



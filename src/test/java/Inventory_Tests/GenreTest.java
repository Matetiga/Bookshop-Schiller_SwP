package Inventory_Tests;

import kickstart.Inventory.Products.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

	@Test
	public void testCreateGenre() {
		Assertions.assertEquals(Genre.getAllGenres().size(), 0);

		Genre genre = Genre.createGenre("Science Fiction");
		Assertions.assertEquals(Genre.getAllGenres().size(), 1);
		assertTrue(Genre.getAllGenres().contains(genre), "Genre not added to the list");

		Genre genre2 = Genre.createGenre("Cooking");
		Genre genre3 = Genre.createGenre("Drama");
		Assertions.assertEquals(Genre.getAllGenres().size(), 3);
		assertTrue(Genre.getAllGenres().contains(genre2), "Genre not added to the list");
		assertTrue(Genre.getAllGenres().contains(genre3), "Genre not added to the list");
	}

	@Test
	public void testCreateDuplicateGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("Science Fiction");
		Assertions.assertEquals(Genre.getAllGenres().size(), 1);
		assertTrue(Genre.getAllGenres().contains(genre2), "Genre not added to the list");
	}

	@Test
	public void testCreateLowerUppercaseDuplicateGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("sCieNceFiction");
		Assertions.assertEquals(Genre.getAllGenres().size(), 1);
	}

	@Test
	public void testDeleteGenre(){
		Genre genre = Genre.createGenre("Science Fiction");
		Genre genre2 = Genre.createGenre("Fantasy");
		Genre genre3 = Genre.createGenre("Horror");
		Assertions.assertEquals(Genre.getAllGenres().size(), 3);
		Genre.deleteGenre(genre);

		Assertions.assertEquals(Genre.getAllGenres().size(), 2);
		assertFalse(Genre.getAllGenres().contains(genre), "Genre not removed from the list");

	}

}



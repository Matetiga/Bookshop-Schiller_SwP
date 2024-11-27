package kickstart.Inventory_Tests;

import kickstart.Inventory.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		for (Genre g: Genre.getAllGenres()){
			System.out.println("testing inside testdeleteGenre" + g.getGenre());
		}
		Genre.deleteGenre(genre);

		Assertions.assertEquals(2, Genre.getAllGenres().size());
		assertFalse(Genre.getAllGenres().contains(genre), "Genre not removed from the list");

	}

}



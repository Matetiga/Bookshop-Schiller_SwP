package kickstart.Inventory_Tests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kickstart.Inventory.AddBookForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


// the same as AddMerchCalendarFormTest
public class AddBookFormTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		// used to have a 'clean' validator for each test
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidForm() {
		AddBookForm form = new AddBookForm(
			"Book",
			"image.png",
			"description",
			new HashSet<>(Set.of("TestGenre")),
			"Author",
			"ISBN",
			"Publisher",
			19.99,
			10
		);

		Set<ConstraintViolation<AddBookForm>> violations = validator.validate(form);
		assertTrue(violations.isEmpty(), "Expected no validation errors for a valid form");
	}

	@Test
	public void testInvalidForm(){
		AddBookForm form = new AddBookForm(
			"",
			null,
			"",
			new HashSet<>(Collections.emptySet()),
			"",
			"",
			"",
			-1,
			-1
		);

		Set<ConstraintViolation<AddBookForm>> violations = validator.validate(form);
		assertFalse(violations.isEmpty(), "Expected validation errors for an invalid form");
	}

	@Test
	public void testGettersAndSetters(){
		AddBookForm form = new AddBookForm();
		form.setName("Book");
		form.setImage("image.png");
		form.setDescription("description");
		form.setGenre(new HashSet<>(Set.of("TestGenre")));
		form.setAuthor("Author");
		form.setISBN("ISBN");
		form.setPublisher("Publisher");
		form.setPrice(19.99);
		form.setStock(10);

		assertEquals("Book", form.getName());
		assertEquals("image.png", form.getImage());
		assertEquals("description", form.getDescription());
		assertEquals(form.getGenre(), new HashSet<>(Set.of("TestGenre")));
		assertEquals("Author", form.getAuthor());
		assertEquals("ISBN", form.getISBN());
		assertEquals("Publisher", form.getPublisher());
		assertEquals(19.99, form.getPrice());
		assertEquals(10, form.getStock());
	}

}

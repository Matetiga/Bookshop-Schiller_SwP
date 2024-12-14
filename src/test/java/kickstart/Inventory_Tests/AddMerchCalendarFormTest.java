package kickstart.Inventory_Tests;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import kickstart.Inventory.AddMerchCalendarForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// this is being tested in a separated file from the controller
// because methods in the controller use 'BindingResult'
public class AddMerchCalendarFormTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		// used to have a 'clean' validator for each test
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidForm() {
		AddMerchCalendarForm form = new AddMerchCalendarForm(
			"T-Shirt",
			"image.png",
			19.99,
			"High quality T-shirt",
			10
		);

		// set with all the violations of the form
		// validate will scan for the violations and add them to the set
		Set<ConstraintViolation<AddMerchCalendarForm>> violations = validator.validate(form);
		assertTrue(violations.isEmpty(), "Expected no validation errors for a valid form");
	}

	@Test
	public void testInvalidForm(){
		AddMerchCalendarForm form = new AddMerchCalendarForm(
			"",
			null,
			-1,
			"",
			-1
		);

		Set<ConstraintViolation<AddMerchCalendarForm>> violations = validator.validate(form);
		assertEquals(5, violations.size(), "Expected 5 validation errors for an invalid form");
	}

	@Test
	public void testGGettersAndSetters(){
		AddMerchCalendarForm form = new AddMerchCalendarForm();
		form.setName("T-Shirt");
		form.setImage("image.png");
		form.setPrice(19.99);
		form.setDescription("High quality T-shirt");
		form.setStock(10);

		assertEquals("T-Shirt", form.getName(), "Expected name to be T-Shirt");
		assertEquals("image.png", form.getImage(), "Expected image to be image.png");
		assertEquals(19.99, form.getPrice(), 0.01, "Expected price to be 19.99");
		assertEquals("High quality T-shirt", form.getDescription(), "Expected description to be High quality T-shirt");
		assertEquals(10, form.getStock(), "Expected stock to be 10");
	}
}


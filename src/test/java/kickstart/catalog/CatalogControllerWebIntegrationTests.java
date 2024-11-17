package kickstart.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerWebIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired CatalogController controller;

	@Test
	void booksMvcIntegrationTest() throws Exception {

		mvc.perform(get("/books")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	void calendersMvcIntegrationTest() throws Exception {

		mvc.perform(get("/calenders")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	void merchMvcIntegrationTest() throws Exception {

		mvc.perform(get("/merch")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}
}

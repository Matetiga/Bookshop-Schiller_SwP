package kickstart.catalog;

import kickstart.Achievement.Achievement;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerWebIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired CatalogController controller;
	@MockBean
	private UserManagement userManagement;
	private User mockUser;

	private Achievement ach1;

	@BeforeEach
	void setUp() {
		UserAccount mockUserAccount = Mockito.mock(UserAccount.class);
		Mockito.when(mockUserAccount.getUsername()).thenReturn("testUser");

		mockUser = new User(mockUserAccount, "Test Address", "Test Name", "Test Last Name", "01.01.1990");
		ach1 = new Achievement("Test Achievement", "Test Description", Role.of("ADMIN"));

		when(userManagement.findByUsername("testUser")).thenReturn(mockUser);
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	void booksMvcIntegrationTest() throws Exception {

		mvc.perform(get("/books")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	void calendersMvcIntegrationTest() throws Exception {

		mvc.perform(get("/calenders")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(username = "testUser", roles="ADMIN")
	void merchMvcIntegrationTest() throws Exception {

		mvc.perform(get("/merch")) //
			.andExpect(status().isOk()) //
			.andExpect(model().attributeExists("catalog")) //
			.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}
}

//package kickstart.catalog;
//
//import kickstart.Achievement.Achievement;
//import kickstart.Inventory.ShopProductCatalog;
//import kickstart.user.User;
//import kickstart.user.UserManagement;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.salespointframework.accountancy.Accountancy;
//import org.salespointframework.useraccount.Role;
//import org.salespointframework.useraccount.UserAccount;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.modulith.test.ApplicationModuleTest;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
//public class CatalogModuleTests {
//	@Autowired
//	ShopProductCatalog catalog;
//	@Autowired ConfigurableApplicationContext context;
//	@MockBean
//	private UserManagement userManagement;
//	private User mockUser;
//
//	private Achievement ach1;
//
//	@BeforeEach
//	void setUp() {
//		UserAccount mockUserAccount = Mockito.mock(UserAccount.class);
//		Mockito.when(mockUserAccount.getUsername()).thenReturn("testUser");
//
//		mockUser = new User(mockUserAccount, "Test Address", "Test Name", "Test Last Name", "01.01.1990");
//		ach1 = new Achievement("Test Achievement", "Test Description", Role.of("ADMIN"));
//
//		when(userManagement.findByUsername("testUser")).thenReturn(mockUser);
//	}
//
//	@Test
//	@WithMockUser(username = "testUser", roles="ADMIN")
//		/*
//		 * Dieser Test stellt sicher, dass das Catalog-Modul korrekt gestartet wird, ohne die Accountancy-Bean zu laden.
//		 * Dadurch wird bestätigt, dass das Catalog-Modul keine unerwünschte Abhängigkeit zu Accountancy hat und sauber isoliert ist.
//		 * */
//	void verifiesModuleBootstrapped() {
//
//		AssertableApplicationContext assertable = AssertableApplicationContext.get(() -> context);
//
//		assertThat(assertable).doesNotHaveBean(Accountancy.class);
//	}
//}

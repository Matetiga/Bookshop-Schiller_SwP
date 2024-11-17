package kickstart.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import kickstart.AbstractIntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@AutoConfigureMockMvc
class UserControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserController controller;
    @Autowired MockMvc mockMvc;

	@Test
	void rejectsUnauthenticatedAccessToController() {

		assertThatExceptionOfType(AuthenticationException.class) 
				.isThrownBy(() -> controller.customerOverview(new ExtendedModelMap()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.customerOverview(model);

		assertThat(model.get("customers")).isNotNull();
	}

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testEmployeeOverview() throws Exception {
        
        mockMvc.perform(get("/employee-overview"))
                .andExpect(status().isOk())  
                .andExpect(view().name("employee-overview"))  
                .andExpect(model().attributeExists("employees"));  
    }

    
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testFinanceOverview() throws Exception {
        mockMvc.perform(get("/finance-overview"))
                .andExpect(status().isOk())  
                .andExpect(view().name("finance-overview"));  
    }

    
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testOrderOverview() throws Exception {
        mockMvc.perform(get("/order-overview"))
                .andExpect(status().isOk())  
                .andExpect(view().name("order-overview"));  
    }

    
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testAdminOverview() throws Exception {
        mockMvc.perform(get("/admin-overview"))
                .andExpect(status().isOk())  
                .andExpect(view().name("admin-overview"))  
                .andExpect(model().attributeExists("admins"));  
    }

    @Test
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void testEmployeeOverviewForbidden() throws Exception {
        mockMvc.perform(get("/employee-overview"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void testFinanceOverviewForbidden() throws Exception {
        mockMvc.perform(get("/finance-overview"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void testOrderOverviewForbidden() throws Exception {
        mockMvc.perform(get("/order-overview"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void testAdminOverviewForbidden() throws Exception {
        mockMvc.perform(get("/admin-overview"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testEmployeeOverviewUnauthorized() throws Exception {
        mockMvc.perform(get("/employee-overview"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    void testFinanceOverviewUnauthorized() throws Exception {
        mockMvc.perform(get("/finance-overview"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    void testOrderOverviewUnauthorized() throws Exception {
        mockMvc.perform(get("/order-overview"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    void testAdminOverviewUnauthorized() throws Exception {
        mockMvc.perform(get("/admin-overview"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testAccountProfilAuthenticated() throws Exception {
        mockMvc.perform(get("/account"))
               .andExpect(status().isOk())  
               .andExpect(view().name("account")) 
               .andExpect(model().attributeExists("user"));  
    }

    @Test
    void testAccountOverviewUnauthenticated() throws Exception {
        mockMvc.perform(get("/account"))
               .andExpect(status().is3xxRedirection())  
               .andExpect(redirectedUrl("/login"));  
    }

}
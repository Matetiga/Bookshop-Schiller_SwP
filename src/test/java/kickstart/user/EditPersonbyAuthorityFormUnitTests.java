package kickstart.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

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

import jakarta.servlet.ServletException;

@SpringBootTest
@AutoConfigureMockMvc
class EditPersonbyAuthorityFormUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagement userManagement;

    @Test
    @WithMockUser(roles = "ADMIN")
    void goToEditPageForEmployeeWithValidUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = mockUser(userId, "employee");

        when(userManagement.safeUserGetByID(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/authority_edit/employee/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("authority_edit"))
                .andExpect(model().attributeExists("editPersonbyAuthorityForm"))
                .andExpect(model().attribute("source", "/authority_edit/employee/" + userId));
    }

    @Test
    @WithMockUser(username = "invalidUser", roles = "ADMIN")
    void editPageThrowsExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
    
        // Simuliere, dass der Benutzer nicht gefunden wird
        when(userManagement.safeUserGetByID(userId)).thenReturn(null);
    
        // Teste, ob die ServletException geworfen wird, wenn der Benutzer nicht existiert
        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/authority_edit/employee/{id}", userId))  // Aufruf der URL mit dem User ID
                    .andExpect(status().isOk()); // Status 200 wird erwartet, aber Exception wird geworfen
        });
    
        // Überprüfe die Ursache der ServletException
        Throwable cause = exception.getCause();
        assertNotNull(cause, "The cause of the ServletException should not be null");
        assertTrue(cause instanceof IllegalStateException, "The cause should be an IllegalStateException");
    
        // Überprüfen der Fehlermeldung der IllegalStateException
        assertEquals("User has to exists, but can't find in UserRepository", cause.getMessage());
    }
    



    @Test
    @WithMockUser(roles = "ADMIN")
    void postEditPageWithValidInputRedirectsToOverview() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = mockUser(userId, "employee");

        when(userManagement.safeUserGetByID(userId)).thenReturn(mockUser);
        doNothing().when(userManagement).editProfilebyAuthority(eq(mockUser), any(EditPersonbyAuthorityForm.class));

        mockMvc.perform(post("/authority_edit/employee/{id}", userId)
                .param("new_name", "Updated Name")
                .param("new_last_name", "Updated Last Name")
                .param("new_address", "Updated Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee-overview"));
    }


    private User mockUser(UUID id, String role) {
        UserAccount mockAccount = Mockito.mock(UserAccount.class);
        when(mockAccount.getUsername()).thenReturn("testUser");

        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserAccount()).thenReturn(mockAccount);
        when(mockUser.getName()).thenReturn("Test Name");
        when(mockUser.getLast_name()).thenReturn("Test Last Name");
        when(mockUser.getAddress()).thenReturn("Test Address");
        when(mockUser.getHighestRole()).thenReturn(Role.of(role));

        return mockUser;
    }
}

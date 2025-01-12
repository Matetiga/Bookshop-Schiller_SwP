package kickstart.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import kickstart.Service.UserAchievementService;
import org.springframework.ui.Model;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import kickstart.Achievement.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import jakarta.servlet.ServletException;

@SpringBootTest
@AutoConfigureMockMvc
class EditUserProfilFormUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagement userManagement;

	private User mockUser;

	@MockBean
	private UserAchievementService userAchievementService;

	@BeforeEach
	void setUp() {
		// Mock UserAccount erstellen
		UserAccount mockUserAccount = Mockito.mock(UserAccount.class);

		// UserAccount Eigenschaften definieren
		Mockito.when(mockUserAccount.getUsername()).thenReturn("testUser");

		// Mock User erstellen
		mockUser = new User(mockUserAccount, "Test Address", "Test Name", "Test Last Name", "01.01.1990");

		// Mock UserDetails
		UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
		when(mockUserDetails.getUsername()).thenReturn("testUser");
		when(userManagement.findByUserDetails(mockUserDetails)).thenReturn(mockUser);

		// Create Authentication token with mocked UserDetails
		Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Mock processAchievement to do nothing
		doNothing().when(userAchievementService).processAchievement(any(UserDetails.class), any(Achievement.class), any(Model.class));
	}

    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void accountPageIsAccessible() throws Exception {
        // Rückgabewert für findAll() mocken
        when(userManagement.findAll()).thenReturn(Streamable.of(mockUser));

        // Test ausführen
        mockMvc.perform(get("/account"))
               .andExpect(status().isOk()) // Prüft, ob der Status 200 OK ist
               .andExpect(view().name("account")); // Prüft, ob die View den Namen "account" hat
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void accountEditPageIsAccessible() throws Exception {
        // Rückgabewert für findAll() mocken
        when(userManagement.findByUsername("testUser")).thenReturn(mockUser);


        EditUserProfilForm mockForm = Mockito.mock(EditUserProfilForm.class);
        mockForm.setEdit_name(mockUser.getName());
        mockForm.setEdit_last_name(mockUser.getLast_name());
        mockForm.setEdit_address(mockUser.getAddress());
        mockForm.setEdit_password("");
        mockForm.setEdit_confirmPassword("");

        mockMvc.perform(get("/account_edit"))
           .andExpect(status().isOk()) // Prüft, ob der Status 200 OK ist
           .andExpect(view().name("account_edit")) // Prüft, ob die View den Namen "account_edit" hat
           .andExpect(model().attributeExists("editUserProfilForm")) // Prüft, ob das Formular im Model vorhanden ist
           .andExpect(model().attribute("editUserProfilForm.edit_name", mockForm.getEdit_name())) // Prüft, ob edit_name korrekt gesetzt ist
           .andExpect(model().attribute("editUserProfilForm.edit_last_name", mockForm.getEdit_last_name())) // Prüft, ob edit_last_name korrekt gesetzt ist
           .andExpect(model().attribute("editUserProfilForm.edit_address", mockForm.getEdit_address())) // Prüft, ob edit_address korrekt gesetzt ist
           .andExpect(model().attribute("editUserProfilForm.edit_password", mockForm.getEdit_password())) // Prüft, ob edit_password korrekt gesetzt ist
           .andExpect(model().attribute("editUserProfilForm.edit_confirmPassword", mockForm.getEdit_confirmPassword())); // Prüft, ob edit_confirmPassword korrekt gesetzt ist
    }

    @Test
    @WithMockUser(username = "nonExistentUser", roles = "CUSTOMER")
    void accountEditPageThrowsExceptionForInvalidUser() {
        // Simuliere, dass der Benutzer nicht existiert
        when(userManagement.findByUsername("nonExistentUser")).thenReturn(null);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/account_edit"))
                .andExpect(status().isOk());
        });

        // Überprüfe die Ursache der ServletException
        Throwable cause = exception.getCause();
		System.out.println(cause);
        assertNotNull(cause); // Sicherstellen, dass eine Ursache vorhanden ist
        assertTrue(cause instanceof NullPointerException); // Überprüfen, ob es die erwartete Exception ist

    }


    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void updateProfileRedirectsToAccountOnSuccess() throws Exception {
        // Rückgabewert für findByUsername mocken
        when(userManagement.findByUsername("testUser")).thenReturn(mockUser);

        // Mock für editProfile
        doNothing().when(userManagement).editProfile(Mockito.eq(mockUser), any(), any());

        // Validen EditUserProfilForm posten
        mockMvc.perform(post("/account_edit")
            .param("edit_name", "New Name")
            .param("edit_last_name", "New Last Name")
            .param("edit_address", "New Address")
            .param("edit_password", "ValidPass1!")
            .param("edit_confirmPassword", "ValidPass1!"))
            .andExpect(status().is3xxRedirection()) // Erwartet eine Umleitung
            .andExpect(redirectedUrl("/account")); // Prüft, ob zur Account-Seite umgeleitet wurde
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void updateProfileReturnsToEditPageOnValidationErrors() throws Exception {
        // Rückgabewert für findByUsername mocken
        when(userManagement.findByUsername("testUser")).thenReturn(mockUser);

        // Ungültigen EditUserProfilForm posten (z.B. Passwort zu kurz)
        mockMvc.perform(post("/account_edit")
            .param("edit_name", "New Name")
            .param("edit_last_name", "New Last Name")
            .param("edit_address", "New Address")
            .param("edit_password", "short") // Ungültiges Passwort
            .param("edit_confirmPassword", "short"))
            .andExpect(status().isOk()) // Erwartet Status 200 (View bleibt auf der gleichen Seite)
            .andExpect(view().name("account_edit")) // Bleibt auf der Bearbeitungsseite
            .andExpect(model().attributeExists("editUserProfilForm")) // Prüft, ob das Formular existiert
            .andExpect(model().attributeHasFieldErrors("editUserProfilForm", "edit_password")); // Passwort-Validierungsfehler
    }

    @Test
    @WithMockUser(username = "invalidUser", roles = "CUSTOMER")
    void updateProfileThrowsExceptionForInvalidUser() throws Exception {
        // Kein User für den gegebenen Benutzernamen finden
        when(userManagement.findByUsername("invalidUser")).thenReturn(null);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/account_edit")
            .param("edit_name", "New Name")
            .param("edit_last_name", "New Last Name")
            .param("edit_address", "New Address")
            .param("edit_password", "ValidPass1!")
            .param("edit_confirmPassword", "ValidPass1!"))
            .andExpect(status().isOk());
        });

        // Überprüfe die Ursache der ServletException
        Throwable cause = exception.getCause();
        assertNotNull(cause); // Sicherstellen, dass eine Ursache vorhanden ist
        assertTrue(cause instanceof IllegalStateException); // Überprüfen, ob es die erwartete Exception ist

        // Überprüfen der Fehlermeldung der IllegalStateException
        assertEquals("User have to exists, but exists not.", cause.getMessage());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void updateProfileReturnsToEditPageOnPasswordMismatch() throws Exception {
        // Rückgabewert für findByUsername mocken
        when(userManagement.findByUsername("testUser")).thenReturn(mockUser);

        // Ungültigen EditUserProfilForm posten (z.B. Passwort und Bestätigungspasswort stimmen nicht überein)
        mockMvc.perform(post("/account_edit")
            .param("edit_name", "New Name")
            .param("edit_last_name", "New Last Name")
            .param("edit_address", "New Address")
            .param("edit_password", "ValidPass1!")
            .param("edit_confirmPassword", "DifferentPass2!"))
            .andExpect(status().isOk())
            .andExpect(view().name("account_edit")) // Bleibt auf der Bearbeitungsseite
            .andExpect(model().attributeExists("editUserProfilForm")) // Prüft, ob das Formular existiert
            .andExpect(model().attributeHasFieldErrors("editUserProfilForm", "edit_confirmPassword")); // Bestätigungspasswort-Fehler
    }

    @Test
    @WithMockUser(username = "testUser", roles = "CUSTOMER")
    void updateProfileReturnsToEditPageOnMissingRequiredFields() throws Exception {
        // Rückgabewert für findByUsername mocken
        when(userManagement.findByUsername("testUser")).thenReturn(mockUser);

        // Ungültigen EditUserProfilForm posten (z.B. Name fehlt)
        mockMvc.perform(post("/account_edit")
            .param("edit_name", "") // Fehlender Name
            .param("edit_last_name", "New Last Name")
            .param("edit_address", "New Address")
            .param("edit_password", "ValidPass1!")
            .param("edit_confirmPassword", "ValidPass1!"))
            .andExpect(status().isOk()) // Erwartet Status 200 (View bleibt auf der gleichen Seite)
            .andExpect(view().name("account_edit")) // Bleibt auf der Bearbeitungsseite
            .andExpect(model().attributeExists("editUserProfilForm")) // Prüft, ob das Formular existiert
            .andExpect(model().attributeHasFieldErrors("editUserProfilForm", "edit_name")); // Name-Validierungsfehler
    }
}

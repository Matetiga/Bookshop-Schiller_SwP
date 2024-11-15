package kickstart.user;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;


@Controller
class UserController {

	private final UserManagement userManagement;

	UserController(UserManagement userManagement) {
		Assert.notNull(userManagement, "UserManagement.java must not be null!");
		this.userManagement = userManagement;
	}
	
		@PostMapping("/register")
		String registerNew(@Valid RegistrationForm form, Errors result, Model model) {

			if (result.hasErrors()) {
				return "register";
			}

			try {
				userManagement.createCustomer(form);
			} catch (IllegalArgumentException e) {
				model.addAttribute("usernameError", "Username "+ form.getName() + " already taken");
				return "register";
			}

			return "redirect:/";
		}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/customers")
	@PreAuthorize("hasRole('ADMIN')")
	String Users(Model model) {

		model.addAttribute("customers", userManagement.findAll());

		return "user-overview";
	}

	@PostMapping("/promote/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String promoteUser(@PathVariable("id") UUID id) {
		userManagement.promoteAccountById(id);

		return "redirect:/user-overview";
	}

	@PostMapping("/degrade/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String degradeUser(@PathVariable("id") UUID id) {
		userManagement.degradeAccountById(id);

		return "redirect:/user-overview";
	}

}

package kickstart.user;

import jakarta.validation.Valid;
import kickstart.orders.MyOrderManagement;
import org.salespointframework.useraccount.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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

		if (userManagement.emailExistsAlready(form.getEmail())) {
			result.rejectValue("email", "error.emailExisted", "Email already taken");
		}

		if (!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
		}

		if (result.hasErrors()) {
			return "register";
		}

		userManagement.createCustomer(form);

		return "redirect:/";
	}

	@GetMapping("/register")
	public String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/customer-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String customerOverview(@RequestParam(value = "toastMessage", required = false) String toastMessage, Model model) {
		if (toastMessage != null && !toastMessage.isEmpty()) {
			model.addAttribute("toastMessage", toastMessage);
		}
		HashSet<User> customers =  new HashSet<>();
		//only customers should be displayed
		for (User user: userManagement.findAll()){
			if (user.getHighestRole().equals(Role.of("CUSTOMER")))
				customers.add(user);
		}
		model.addAttribute("customers", customers);

		return "customer-overview";
	}

	@GetMapping("/employee-overview")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	String employeeOverview(@RequestParam(value = "toastMessage", required = false) String toastMessage, Model model){
		if (toastMessage != null && !toastMessage.isEmpty()) {
			model.addAttribute("toastMessage", toastMessage);
		}

		HashSet<User> employees =  new HashSet<>();
		//only employees should be displayed
		for (User user: userManagement.findAll()){
			if (user.getHighestRole().equals(Role.of("EMPLOYEE")))
				employees.add(user);
		}
		model.addAttribute("employees", employees);

		return "employee-overview";
	}

	@GetMapping("/admin-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String Admins(@RequestParam(value = "toastMessage", required = false) String toastMessage, Model model){
		if (toastMessage != null && !toastMessage.isEmpty()) {
			model.addAttribute("toastMessage", toastMessage);
		}
		HashSet<User> admins =  new HashSet<>();
		//only admins should be displayed
		for (User user: userManagement.findAll()){
			if (user.getHighestRole().equals(Role.of("ADMIN")))
				admins.add(user);
		}
		model.addAttribute("admins", admins);

		return "admin-overview";
	}

	@PostMapping("/promote/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String promoteUser(@PathVariable("id") UUID id, @RequestParam String source, Model model) {
		/*
		When a User gets promoted there should be a confirmation message that is saved below.
		However, we reload the site after each button click. The message wouldnt be visible. That's why we add the message as URL parameter to pass this information,
		so that on reload, we can check if a toastMessage has been left. If so => display it
		 */
		String toastMessage = userManagement.promoteAccountById(id);
		return "redirect:/" + source + "?toastMessage=" + toastMessage;
	}

	@PostMapping("/degrade/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String degradeUser(@PathVariable("id") UUID id, @RequestParam String source, RedirectAttributes redirectAttributes) {
		String toastMessage = userManagement.degradeAccountById(id);
		redirectAttributes.addFlashAttribute("toastMessage", toastMessage);
		return "redirect:/" + source + "?toastMessage=" + toastMessage;
	}

	@GetMapping("/account")
	public String accountOverview(@AuthenticationPrincipal UserDetails UserDetails, Model model) {

		if (UserDetails == null) return "redirect:/login";


		for (User user : userManagement.findAll()) {
			if (user.getUserAccount().getUsername().equals(UserDetails.getUsername())) {
				model.addAttribute("user", user);
				return "account";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/account_edit")
	public String accountEdit(@AuthenticationPrincipal UserDetails UserDetails, EditUserProfilForm form, Model model) {

		if (UserDetails == null) throw new IllegalStateException("User has to exists, but does not exist");

		User user = userManagement.findByUsername(UserDetails.getUsername());
		if (user == null) {
			throw new IllegalStateException("User has to exists, but can't find in UserRepository");
		}
		else {
			form.setEdit_name(user.getName());
			form.setEdit_last_name(user.getLast_name());
			form.setEdit_address(user.getAddress());
			form.setEdit_password("");
			form.setEdit_confirmPassword("");
			model.addAttribute("editUserProfileForm", form);
			return "account_edit";
		}
	}

	@PostMapping("/account_edit")
	String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid EditUserProfilForm form, Errors result, Model model) {

		if (userDetails == null) throw new IllegalStateException("User have to exists, but does not.");

		User user = userManagement.findByUsername(userDetails.getUsername());
		 
		if (user == null) throw new IllegalArgumentException("User have to exists, but exists not.");

		if (result.hasErrors()) {
			model.addAttribute("editUserProfileForm", form);
			return "account_edit"; 
		}
		else {
			userManagement.editProfile(user, user.getUserAccount(), form);
			return "redirect:/account";
		}
	}

	public UserManagement getUserManagement() {
		return userManagement;
	}
}

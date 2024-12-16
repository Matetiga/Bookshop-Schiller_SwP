package kickstart.user;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;




@Controller
class UserController {

	private final UserManagement userManagement;
	@Autowired
	private MessageSource messageSource;

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
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
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

		String states = messageSource.getMessage("order.states", null, LocaleContextHolder.getLocale());

		model.addAttribute("customers", customers);
		model.addAttribute("selectedState", "Alle");
		model.addAttribute("statesList", states.split(","));

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
			model.addAttribute("editUserProfilForm", form);
			model.addAttribute("source", "/account_edit"); //for dynamic post button (is id present or not)
			return "account_edit";
		}
	}

	@PostMapping("/account_edit")
	String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid EditUserProfilForm form, Errors result, Model model) {

		if (userDetails == null) throw new IllegalStateException("User have to exists, but does not.");

		User user = userManagement.findByUsername(userDetails.getUsername());

		return pushEditOfUserAccount(form, result, model, user);
	}

	public UserManagement getUserManagement() {
		return userManagement;
	}


	@GetMapping("/account_edit/employee/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String goToAccountEditOfEmployees(@PathVariable("id") UUID id, EditUserProfilForm form, Model model){
		User user = userManagement.safeUserGetByID(id);
		String userType = "employee";


		return addUserEditInformationToModelAndRedirect(id, form, model, user, userType);
	}

	@PostMapping("/account_edit/employee/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String doAccountEditOfEmployees(@PathVariable("id") UUID id,  @Valid EditUserProfilForm form, Errors result, Model model){
		User user = userManagement.safeUserGetByID(id);

		if (pushEditOfUserAccount(form, result, model, user).equals("redirect:/account")){
			return "redirect:/employee-overview";
		}

		return pushEditOfUserAccount(form, result, model, user);
	}

	@NotNull
	private String pushEditOfUserAccount(@Valid EditUserProfilForm form, Errors result, Model model, User user) {
		if (user == null) {
			throw new IllegalStateException("User have to exists, but exists not.");
		}

		if (!form.getEdit_password().equals(form.getEdit_confirmPassword())) {
			result.rejectValue("edit_confirmPassword", "error.edit_confirmPassword", "Passwords do not match");
		}

		if (result.hasErrors()) {
			model.addAttribute("editUserProfilForm", form);
			return "account_edit";
		}

		userManagement.editProfile(user, user.getUserAccount(), form);
		return "redirect:/account";
	}

	@GetMapping("/account_edit/customer/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String goToAccountEditOfCustomer(@PathVariable("id") UUID id, EditUserProfilForm form, Model model){
		User user = userManagement.safeUserGetByID(id);
		String userType = "customer";

		return addUserEditInformationToModelAndRedirect(id, form, model, user, userType);
	}

	@NotNull
	private String addUserEditInformationToModelAndRedirect(@PathVariable("id") UUID id, EditUserProfilForm form, Model model, User user, String userType) {
		if (user == null) {
			throw new IllegalStateException("User has to exists, but can't find in UserRepository");
		}
		else {
			form.setEdit_name(user.getName());
			form.setEdit_last_name(user.getLast_name());
			form.setEdit_address(user.getAddress());
			form.setEdit_password("");
			form.setEdit_confirmPassword("");
			model.addAttribute("editUserProfilForm", form);
			model.addAttribute("source", "/account_edit/" + userType + "/" + id); //for dynamic post button (is id present or not)
		}

		return "account_edit";
	}

	@PostMapping("/account_edit/customer/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String doAccountEditOfCustomer(@PathVariable("id") UUID id,  @Valid EditUserProfilForm form, Errors result, Model model){
		User user = userManagement.safeUserGetByID(id);

		if (pushEditOfUserAccount(form, result, model, user).equals("redirect:/account")){
			return "redirect:/customer-overview";
		}

		return pushEditOfUserAccount(form, result, model, user);
	}

}

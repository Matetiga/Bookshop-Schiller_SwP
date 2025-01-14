package kickstart.user;

import jakarta.validation.Valid;
import kickstart.Achievement.Achievement;
import kickstart.Service.UserAchievementService;
import org.jetbrains.annotations.NotNull;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final UserAchievementService userAchievementService;


	UserController(UserManagement userManagement) {
		Assert.notNull(userManagement, "UserManagement.java must not be null!");
		this.userManagement = userManagement;
		this.userAchievementService = new UserAchievementService(this.userManagement);
	}

	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result) {

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
	public String register(RegistrationForm form, Model model) {
		return "register";
	}

	@GetMapping("/customer-overview")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	String customerOverview(@RequestParam(value = "toastMessage", required = false) String toastMessage, Model model) {
		if (toastMessage != null && !toastMessage.isEmpty()) {
			model.addAttribute("toastMessage", toastMessage);
		}
		List<User> customers= userManagement.getAllUsersOfRole(Role.of("CUSTOMER"));


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

		List<User> employees= userManagement.getAllUsersOfRole(Role.of("EMPLOYEE"));
		model.addAttribute("employees", employees);

		return "employee-overview";
	}

	@GetMapping("/admin-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String Admins(@RequestParam(value = "toastMessage", required = false) String toastMessage, Model model){
		if (toastMessage != null && !toastMessage.isEmpty()) {
			model.addAttribute("toastMessage", toastMessage);
		}
		List<User> admins= userManagement.getAllUsersOfRole(Role.of("ADMIN"));
		model.addAttribute("admins", admins);

		return "admin-overview";
	}

	@PostMapping("/promote/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String promoteUser(@PathVariable("id") UUID id, @RequestParam String source) {
		/*
		When a User gets promoted there should be a confirmation message that is saved below.
		However, we reload the site after each button click. The message wouldn't be visible. That's why we add the message as URL parameter to pass this information,
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
	public String accountEdit(@AuthenticationPrincipal UserDetails userDetails, EditUserProfilForm form, Model model) {

		User user = userManagement.findByUserDetails(userDetails);

		if (user == null) {
			throw new IllegalStateException("User have to exists, but does not.");
		}

		Achievement achievement = new Achievement("Ändere wer du bist!", "Zum ersten mal auf das Account Edit gekommen!", Role.of("CUSTOMER"));
		userAchievementService.processAchievement(userDetails, achievement, model);

		form.setEdit_name(user.getName());
		form.setEdit_last_name(user.getLast_name());
		form.setEdit_address(user.getAddress());
		form.setEdit_password("");
		form.setEdit_confirmPassword("");
		model.addAttribute("editUserProfilForm", form);

		System.out.println("Model attributes: " + model.asMap());

		return "account_edit";

	}

	@PostMapping("/account_edit")
	String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid EditUserProfilForm form, Errors result, Model model) {

		if (userDetails == null){
			throw new IllegalStateException("User have to exists, but does not.");
		}
		User user = userManagement.findByUsername(userDetails.getUsername());
		
		if (user == null) throw new IllegalStateException("User have to exists, but exists not.");


		if (!form.getEdit_password().equals(form.getEdit_confirmPassword())) {
			result.rejectValue("edit_confirmPassword", "error.edit_confirmPassword", "Passwords do not match");
		}

		if (result.hasErrors()) {
			model.addAttribute("editUserProfilForm", form);
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


	@GetMapping("/authority_edit/employee/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String goToAccountEditOfEmployees(@PathVariable("id") UUID id, EditPersonbyAuthorityForm form, Model model){
		User user = userManagement.safeUserGetByID(id);
		String userType = "employee";


		return addUserEditInformationToModelAndRedirect(id, form, model, user, userType);
	}

	@PostMapping("/authority_edit/employee/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String doAccountEditOfEmployees(@PathVariable("id") UUID id,  @Valid EditPersonbyAuthorityForm form, Errors result, Model model){
		User user = userManagement.safeUserGetByID(id);
		return pushEditOfUserAccount(form, result, model, user);
	}

	@NotNull
	private String pushEditOfUserAccount(@Valid EditPersonbyAuthorityForm form, Errors result, Model model, User user) {
		if (user == null) {
			throw new IllegalStateException("User have to exists, but exists not.");
		}

		if (result.hasErrors()) {
			model.addAttribute("editPersonbyAuthorityForm", form);
			return "authority_edit";
		}
		else {
			userManagement.editProfilebyAuthority(user, form);
		}
		if (user.getHighestRole().equals(Role.of("CUSTOMER"))) {
			return "redirect:/customer-overview";
		}
		else return "redirect:/employee-overview";
	}

	@GetMapping("/authority_edit/customer/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String goToAccountEditOfCustomer(@PathVariable("id") UUID id, EditPersonbyAuthorityForm form, Model model){
		User user = userManagement.safeUserGetByID(id);
		String userType = "customer";

		return addUserEditInformationToModelAndRedirect(id, form, model, user, userType);
	}

	@NotNull
	private String addUserEditInformationToModelAndRedirect(@PathVariable("id") UUID id, EditPersonbyAuthorityForm form, Model model, User user, String userType) {
		if (user == null) {
			throw new IllegalStateException("User has to exists, but can't find in UserRepository");
		}
		else {
			form.setnew_name(user.getName());
			form.setnew_last_name(user.getLast_name());
			form.setnew_address(user.getAddress());
			model.addAttribute("editPersonbyAuthorityForm", form);
			model.addAttribute("source", "/authority_edit/" + userType + "/" + id); //for dynamic post button (is id present or not)
		}

		return "authority_edit";
	}

	@PostMapping("/authority_edit/customer/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String doAccountEditOfCustomer(@PathVariable("id") UUID id,  @Valid EditPersonbyAuthorityForm form, Errors result, Model model){
		User user = userManagement.safeUserGetByID(id);
		return pushEditOfUserAccount(form, result, model, user);
	}

	@GetMapping("/customer-overview/filterByDescendingAlphabet")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String sortByDescendingAlphabet(Model model) {
		List<User> customers= userManagement.getAllUsersOfRole(Role.of("CUSTOMER"));

		customers.sort((user1, user2) -> user1.getFullName().compareToIgnoreCase(user2.getFullName()));
		Collections.reverse(customers);

		String states = messageSource.getMessage("order.states", null, LocaleContextHolder.getLocale());

		model.addAttribute("customers", customers);
		model.addAttribute("selectedState", "Alle");
		model.addAttribute("statesList", states.split(","));

		return "customer-overview";
	}

	@GetMapping("/customer-overview/filterByAscendingAlphabet")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public String sortByAscendingAlphabet(Model model) {
		List<User> customers= userManagement.getAllUsersOfRole(Role.of("CUSTOMER"));

		customers.sort((user1, user2) -> user1.getFullName().compareToIgnoreCase(user2.getFullName()));

		String states = messageSource.getMessage("order.states", null, LocaleContextHolder.getLocale());

		model.addAttribute("customers", customers);
		model.addAttribute("selectedState", "Alle");
		model.addAttribute("statesList", states.split(","));

		return "customer-overview";
	}


	@GetMapping("/achievements")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
	String achievements(@AuthenticationPrincipal UserDetails userDetails, Model model){
		Achievement achievement = new Achievement("Achievement Hunter!", "Besuche deine gesammelten und sehr sinnvollen Achievements ;D!", Role.of("CUSTOMER"));

		userAchievementService.processAchievement(userDetails, achievement, model);
		userAchievementService.addAllAchievementsToModel(userDetails, model);
		userAchievementService.addProgressToModel(userDetails, model);

		return "achievements";
	}

}

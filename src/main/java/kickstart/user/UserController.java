package kickstart.user;

import jakarta.validation.Valid;
import org.salespointframework.useraccount.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.*;



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
	
	// until yet not perfect style 
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
		else {
			// ?
		}
		userManagement.createCustomer(form);

		return "redirect:/";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/customer-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String customerOverview(Model model) {
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
	String employeeOverview(Model model) {
		HashSet<User> employees =  new HashSet<>();
		//only employees should be displayed
		for (User user: userManagement.findAll()){
			if (user.getHighestRole().equals(Role.of("EMPLOYEE")))
				employees.add(user);
		}
		model.addAttribute("employees", employees);

		return "employee-overview";
	}

	@GetMapping("/finance-data")
	@ResponseBody
	public Map<String, Double> getMonthlyRevenueData() {

		// Einnahmen nach Monaten
		Map<String, Double> revenueData = new LinkedHashMap<>();
		revenueData.put("August", 420.75);
		revenueData.put("September", 344.87);
		revenueData.put("Oktober", 556.98);
		revenueData.put("November", 690.56);

		return revenueData;
	}

	@GetMapping("/finance-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String financeOverview(Model model) {
		return "finance-overview";
	}


	@GetMapping("/admin-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String Admins(Model model) {
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
	public String promoteUser(@PathVariable("id") UUID id, @RequestParam String source) {
		userManagement.promoteAccountById(id);

		return "redirect:/" + source;
	}

	@PostMapping("/degrade/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String degradeUser(@PathVariable("id") UUID id, @RequestParam String source) {


		userManagement.degradeAccountById(id);

		return "redirect:/" + source;
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


	public UserManagement getUserManagement() {
		return userManagement;
	}
}

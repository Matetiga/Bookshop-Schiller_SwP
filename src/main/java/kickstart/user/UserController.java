/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kickstart.user;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
			model.addAttribute("usernameError", "Username already taken");
			return "register";
		}

		return "redirect:/";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/Users")
	@PreAuthorize("hasRole('ADMIN')")
	String Users(Model model) {

		model.addAttribute("UserList", userManagement.findAll());

		return "Users";
	}
}

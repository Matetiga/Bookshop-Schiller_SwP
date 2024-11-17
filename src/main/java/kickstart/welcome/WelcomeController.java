/*
 * Copyright 2014-2023 the original author or authors.
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
package kickstart.welcome;

import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class WelcomeController {

 	@GetMapping("/")
// 	@GetMapping("/ignore")
 	public String index() {
		 return "homeProvisorisch";
// 		return "catalog_books";
 	}

	@PostMapping("/welcome")
	String welcome(){
		return "homeProvisorisch";
	}

	@PostMapping("/login")
	public String redirectLogin() {
		return ("redirect:/login");
	}

	@PostMapping("/cart")
	public String redirectCart() {
		return ("redirect:/cart");
	}

	@PostMapping("/orders")
	public String redirectOrders() {
		return ("redirect:/orderview");
	}
}
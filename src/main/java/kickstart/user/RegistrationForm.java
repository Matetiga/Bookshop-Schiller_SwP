/*
 * Copyright 2013-2021 the original author or authors.
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

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;
import org.springframework.validation.Errors;

class RegistrationForm {

	private final @NotEmpty String name;
	private final @Size(min = 8, message = "Password must be at least 8 characters") String password;
	private final @NotEmpty String address;
	private final String confirmPassword;

	public RegistrationForm(String name, String password, String confirmPassword, String address) {
		this.name = name;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public String getAddress() {
		return address;
	}

	public void validate(Errors errors) {
		if (!password.equals(confirmPassword)) {
			errors.rejectValue("confirmPassword", "Passwords do not match");
		}
	}
}

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

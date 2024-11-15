package kickstart.user;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;

class RegistrationForm {

	private final @NotEmpty(message = "The Username cannot be empty") String username;
	private final @Size(min = 8, message = "Password must be at least 8 characters") String password;
	private final @NotEmpty(message = "The address cannot be empty") String address;
	private final String confirmPassword;

	public RegistrationForm(String username, String password, String confirmPassword, String address) {
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.address = address;
	}

	public String getUsername() {
		return username;
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
}

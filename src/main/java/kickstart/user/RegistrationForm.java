package kickstart.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;


class RegistrationForm {

	private final @Email(message = "It has to be a valid email") String email;
	private final @Size(min = 8, message = "Password must be at least 8 characters") 
				  @Pattern(regexp = ".*[!@#$%^&(),.?\":{}|<>].*", message = "Password must contain at least one special character of: !@#$%^&*(),.?\\\":{}|<>") String password;
	private final @NotEmpty(message = "The address cannot be empty") String address;
	private final String confirmPassword;
	private final @NotEmpty(message = "The name cannot be empty") String name;
	private final @NotEmpty(message = "The last name cannot be empty") String last_name;	 
	private final @NotEmpty(message = "The Birth Date name cannot be empty") String birthDate;

	public RegistrationForm(String email, String password, String confirmPassword, 
							String address, String name, String last_name, String birthDate) {
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.address = address;
		this.name = name;
		this.last_name = last_name;
		this.birthDate = birthDate;
	}

	public String getName() {
		return name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getEmail() {
		return email;
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

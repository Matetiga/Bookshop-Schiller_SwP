package kickstart.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 *
 */
class RegistrationForm {

	private final @Email(message = "It has to be a valid email") String email;
	private final @Size(min = 8, message = "Password must be at least 8 characters") 
				  @Pattern(regexp = ".*[!@#$%^&(),.?\":{}|<>].*",
					  message = "Password must contain at least one special character of: !@#$%^&*(),.?\\\":{}|<>")
	String password;
	private final @NotEmpty(message = "The address cannot be empty") String address;
	private final String confirmPassword;
	private final @NotEmpty(message = "The name cannot be empty") String name;
	private final @NotEmpty(message = "The last name cannot be empty") String lastName;
	private final @NotEmpty(message = "The Birth Date name cannot be empty") String birthDate;

	/**
	 *
	 * @param email
	 * @param password
	 * @param confirmPassword
	 * @param address
	 * @param name
	 * @param last_name
	 * @param birthDate
	 */
	public RegistrationForm(String email, String password, String confirmPassword, 
							String address, String name, String last_name, String birthDate) {
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.address = address;
		this.name = name;
		this.lastName = last_name;
		this.birthDate = birthDate;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return
	 */
	public String getLast_name() {
		return lastName;
	}

	/**
	 *
	 * @return
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *
	 * @return
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 *
	 * @return
	 */
	public String getAddress() {
		return address;
	}
}

package kickstart.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


class EditUserProfilForm {

	private @NotEmpty(message = "The Address cannot be empty") String edit_address;
	private @NotEmpty(message = "The Name cannot be empty") String edit_name;
	private @NotEmpty(message = "The Last Name cannot be empty") String edit_last_name;
	private @Size(min = 8, message = "Password must be at least 8 characters")
	 		@Pattern(regexp = ".*[!@#$%^&(),.?\":{}|<>].*", message = "Password must contain at least one special character of: !@#$%^&*(),.?\\\\\\\":{}|<>") String edit_password;
	private String edit_confirmPassword;

	/**
	 *
	 * @param edit_address
	 * @param edit_name
	 * @param edit_last_name
	 * @param birthDate
	 * @param edit_password
	 * @param edit_confirmPassword
	 */
	public EditUserProfilForm(String edit_address, String edit_name, String edit_last_name, String birthDate, String edit_password, String edit_confirmPassword) {
		this.edit_address = edit_address;
		this.edit_name = edit_name;
		this.edit_last_name = edit_last_name;
		this.edit_password = edit_password;
		this.edit_confirmPassword = edit_confirmPassword;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_name() {
		return edit_name;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_last_name() {
		return edit_last_name;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_address() {
		return edit_address;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_password() {
		return edit_password;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_confirmPassword() {
		return edit_confirmPassword;
	}

	/**
	 *
	 * @param edit_address
	 */
	public void setEdit_address(String edit_address) {
		this.edit_address = edit_address;
	}

	/**
	 *
	 * @param edit_name
	 */
	public void setEdit_name(String edit_name) {
		this.edit_name = edit_name;
	}

	/**
	 *
	 * @param edit_last_name
	 */
	public void setEdit_last_name(String edit_last_name) {
		this.edit_last_name = edit_last_name;
	}

	/**
	 *
	 * @param edit_password
	 */
	public void setEdit_password(String edit_password) {
		this.edit_password = edit_password;
	}

	/**
	 *
	 * @param edit_confirmPassword
	 */
	public void setEdit_confirmPassword(String edit_confirmPassword) {
		this.edit_confirmPassword = edit_confirmPassword;
	}
}

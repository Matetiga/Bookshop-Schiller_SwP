package kickstart.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


class EditUserProfilForm {

	private @NotEmpty(message = "The Address cannot be empty") String editAddress;
	private @NotEmpty(message = "The Name cannot be empty") String editName;
	private @NotEmpty(message = "The Last Name cannot be empty") String editLastName;
	private @Size(min = 8, message = "Password must be at least 8 characters")
	 		@Pattern(regexp = ".*[!@#$%^&(),.?\":{}|<>].*",
				message = "Password must contain at least one special character of: !@#$%^&*(),.?\\\\\\\":{}|<>")
	String editPassword;
	private String editConfirmPassword;

	/**
	 *
	 * @param edit_address
	 * @param edit_name
	 * @param edit_last_name
	 * @param edit_password
	 * @param edit_confirmPassword
	 */
	public EditUserProfilForm(String edit_address, String edit_name, String edit_last_name,
							  String edit_password, String edit_confirmPassword) {
		this.editAddress = edit_address;
		this.editName = edit_name;
		this.editLastName = edit_last_name;
		this.editPassword = edit_password;
		this.editConfirmPassword = edit_confirmPassword;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_name() {
		return editName;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_last_name() {
		return editLastName;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_address() {
		return editAddress;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_password() {
		return editPassword;
	}

	/**
	 *
	 * @return
	 */
	public String getEdit_confirmPassword() {
		return editConfirmPassword;
	}

	/**
	 *
	 * @param edit_address
	 */
	public void setEdit_address(String edit_address) {
		this.editAddress = edit_address;
	}

	/**
	 *
	 * @param edit_name
	 */
	public void setEdit_name(String edit_name) {
		this.editName = edit_name;
	}

	/**
	 *
	 * @param edit_last_name
	 */
	public void setEdit_last_name(String edit_last_name) {
		this.editLastName = edit_last_name;
	}

	/**
	 *
	 * @param edit_password
	 */
	public void setEdit_password(String edit_password) {
		this.editPassword = edit_password;
	}

	/**
	 *
	 * @param edit_confirmPassword
	 */
	public void setEdit_confirmPassword(String edit_confirmPassword) {
		this.editConfirmPassword = edit_confirmPassword;
	}
}

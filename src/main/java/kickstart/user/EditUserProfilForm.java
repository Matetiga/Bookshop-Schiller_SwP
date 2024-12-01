package kickstart.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


class EditUserProfilForm {

	private @NotEmpty(message = "The edit_address cannot be empty") String edit_address;
	private @NotEmpty(message = "The edit_name cannot be empty") String edit_name;
	private @NotEmpty(message = "The last edit_name cannot be empty") String edit_last_name;
	private @Size(min = 8, message = "Password must be at least 8 characters") String edit_password;
	private String edit_confirmPassword;	 


	public EditUserProfilForm(String edit_address, String edit_name, String edit_last_name, String birthDate, String edit_password, String edit_confirmPassword) {
		this.edit_address = edit_address;
		this.edit_name = edit_name;
		this.edit_last_name = edit_last_name;
		this.edit_password = edit_password;
		this.edit_confirmPassword = edit_confirmPassword;
	}

	public String getEdit_name() {
		return edit_name;
	}

	public String getEdit_last_name() {
		return edit_last_name;
	}

	public String getEdit_address() {
		return edit_address;
	}

	public String getEdit_password() {
		return edit_password;
	}

	public String getEdit_confirmPassword() {
		return edit_confirmPassword;
	}

	public void setEdit_address(String edit_address) {
		this.edit_address = edit_address;
	}

	public void setEdit_name(String edit_name) {
		this.edit_name = edit_name;
	}

	public void setEdit_last_name(String edit_last_name) {
		this.edit_last_name = edit_last_name;
	}

	public void setEdit_password(String edit_password) {
		this.edit_password = edit_password;
	}

	public void setEdit_confirmPassword(String edit_confirmPassword) {
		this.edit_confirmPassword = edit_confirmPassword;
	}
}

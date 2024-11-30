package kickstart.user;

import jakarta.validation.constraints.NotEmpty;


class EditUserProfilForm {

	private @NotEmpty(message = "The edit_address cannot be empty") String edit_address;
	private @NotEmpty(message = "The edit_name cannot be empty") String edit_name;
	private @NotEmpty(message = "The last edit_name cannot be empty") String edit_last_name;	 


	public EditUserProfilForm(String edit_address, String edit_name, String edit_last_name, String birthDate) {
		this.edit_address = edit_address;
		this.edit_name = edit_name;
		this.edit_last_name = edit_last_name;
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

	public void setEdit_address(String edit_address) {
		this.edit_address = edit_address;
	}

	public void setEdit_name(String edit_name) {
		this.edit_name = edit_name;
	}

	public void setEdit_last_name(String edit_last_name) {
		this.edit_last_name = edit_last_name;
	}
}

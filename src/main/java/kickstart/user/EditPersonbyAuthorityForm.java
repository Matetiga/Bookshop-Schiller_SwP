package kickstart.user;

import jakarta.validation.constraints.NotEmpty;


class EditPersonbyAuthorityForm {

	private @NotEmpty(message = "The Address cannot be empty") String newAddress;
	private @NotEmpty(message = "The Name cannot be empty") String newName;
	private @NotEmpty(message = "The Last Name cannot be empty") String newLastName;

	/**
	 *
	 * @param new_address
	 * @param new_name
	 * @param new_last_name
	 */
	public EditPersonbyAuthorityForm(String new_address, String new_name, String new_last_name) {
		this.newAddress = new_address;
		this.newName = new_name;
        this.newLastName = new_last_name;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_name() {
		return newName;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_last_name() {
		return newLastName;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_address() {
		return newAddress;
	}

	/**
	 *
	 * @param new_address
	 */
	public void setnew_address(String new_address) {
		this.newAddress = new_address;
	}

	/**
	 *
	 * @param new_name
	 */
	public void setnew_name(String new_name) {
		this.newName = new_name;
	}

	/**
	 *
	 * @param new_last_aame
	 */
	public void setnew_last_name(String new_last_aame) {
		this.newLastName = new_last_aame;
	}
}

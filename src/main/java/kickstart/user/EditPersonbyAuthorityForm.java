package kickstart.user;

import jakarta.validation.constraints.NotEmpty;


class EditPersonbyAuthorityForm {

	private @NotEmpty(message = "The Address cannot be empty") String new_address;
	private @NotEmpty(message = "The Name cannot be empty") String new_name;
	private @NotEmpty(message = "The Last Name cannot be empty") String new_last_name;

	/**
	 *
	 * @param new_address
	 * @param new_name
	 * @param new_last_name
	 */
	public EditPersonbyAuthorityForm(String new_address, String new_name, String new_last_name) {
		this.new_address = new_address;
		this.new_name = new_name;
        this.new_last_name = new_last_name;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_name() {
		return new_name;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_last_name() {
		return new_last_name;
	}

	/**
	 *
	 * @return
	 */
	public String getnew_address() {
		return new_address;
	}

	/**
	 *
	 * @param new_address
	 */
	public void setnew_address(String new_address) {
		this.new_address = new_address;
	}

	/**
	 *
	 * @param new_name
	 */
	public void setnew_name(String new_name) {
		this.new_name = new_name;
	}

	/**
	 *
	 * @param new_last_aame
	 */
	public void setnew_last_name(String new_last_aame) {
		this.new_last_name = new_last_aame;
	}
}

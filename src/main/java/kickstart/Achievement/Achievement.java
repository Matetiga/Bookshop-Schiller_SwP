package kickstart.Achievement;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.salespointframework.useraccount.Role;
import java.util.Objects;

@Entity
public class Achievement {
	private String title;
	private String description;
	private boolean completed;
	private Role lowestRoleNeeded;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 *
	 * @param title
	 * @param description
	 * @param lowestRoleNeeded
	 */
	public Achievement(String title, String description, Role lowestRoleNeeded) {
		this.title = title;
		this.description = description;
		this.completed = false;
		this.lowestRoleNeeded = lowestRoleNeeded;
	}

	/**
	 *
	 */
	public Achievement() {

	}

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @return
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 *
	 * @return
	 */
	public Role getLowest_role_needed() {
		return lowestRoleNeeded;
	}

	/**
	 *
	 * @return
	 */
	//custom hash_code so that Achievements with same title are considered equal in HashSets
	@Override
	public int hashCode() {
		if (title == null) {
			return 0;
		} else {
			return title.hashCode();
		}
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Achievement other = (Achievement) obj;
		return Objects.equals(title, other.title);
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}
}

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
	private Role lowest_role_needed;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Achievement(String title, String description, Role lowest_role_needed) {
		this.title = title;
		this.description = description;
		this.completed = false;
		this.lowest_role_needed = lowest_role_needed;
	}

	public Achievement() {

	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public Role getLowest_role_needed() {
		return lowest_role_needed;
	}

	//custom hash_code so that Achievements with same title are considered equal in HashSets
	@Override
	public int hashCode() {
		if (title == null) {
			return 0;
		} else {
			return title.hashCode();
		}
	}

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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}

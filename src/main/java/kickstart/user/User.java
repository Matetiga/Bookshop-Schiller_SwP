package kickstart.user;

import jakarta.persistence.*;
import kickstart.user.User.UserIdentifier;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import kickstart.Achievement.Achievement;

@Entity
public class User extends AbstractAggregateRoot<UserIdentifier> {
	private @EmbeddedId UserIdentifier id = new UserIdentifier();
	private String address;
	private String name;
	private String last_name;
    private String birthDate;
	private LocalDateTime registrationDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Achievement> achievements = new HashSet<>();


	private @OneToOne UserAccount userAccount;


	/**
	 *
	 */
	@SuppressWarnings("unused")
	private User() {}

	/**
	 *
	 * @param userAccount
	 * @param address
	 * @param name
	 * @param last_name
	 * @param birthDate
	 */
	public User(UserAccount userAccount, String address, String name, String last_name,
				 String birthDate) {
		this.userAccount = userAccount;
		this.address = address;
		this.name = name;
		this.last_name = last_name;
		this.birthDate = birthDate;
		this.registrationDate = LocalDateTime.now();
	}

	/**
	 *
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 *
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 *
	 * @param last_name
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
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
	 * @param birthDate
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 *
	 * @return
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 *
	 * @return
	 */
	public String getFullName() {
		return name + " " + last_name;
	}

	/**
	 *
	 * @return
	 */
	public String getEmail(){
		return userAccount.getUsername();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public UserIdentifier getId() {
		return id;
	}

	/**
	 *
	 * @return
	 */
	public String getRegistrationDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm");

		return this.registrationDate.format(formatter).toString();
	}

	/**
	 *
	 * @return
	 */
	public Role getHighestRole(){
		if (this.getUserAccount().hasRole(Role.of("ADMIN"))) return Role.of("ADMIN");
		if (this.getUserAccount().hasRole((Role.of("EMPLOYEE")))) return Role.of("EMPLOYEE");

		return Role.of("CUSTOMER");
	}

	/**
	 *
	 * @param achievement
	 * @return
	 */
	public boolean hasAchievement(Achievement achievement) {
		return achievements.contains(achievement);
	}

	/**
	 *
	 * @param achievement
	 * @return
	 */
	public boolean achievementCanBeAdded(Achievement achievement) {
		Role userRole = this.getHighestRole();
		Role requiredRole = achievement.getLowest_role_needed();

		List<Role> roleHierarchy = Arrays.asList(Role.of("CUSTOMER"), Role.of("EMPLOYEE"), Role.of("ADMIN"));

		int userRoleIndex = roleHierarchy.indexOf(userRole);
		int requiredRoleIndex = roleHierarchy.indexOf(requiredRole);

		return requiredRoleIndex >= 0 && userRoleIndex >= requiredRoleIndex;
	}

	/**
	 *
	 * @param achievement
	 */
	public void addAchievement(Achievement achievement) {
		System.out.println("Before: " + achievements);
		achievements.add(achievement);
		System.out.println("After: " + achievements);
		System.out.println("User HashCode: " + System.identityHashCode(this));
	}

	/**
	 *
	 * @return
	 */
	public Set<Achievement> getAchievements() {
		return achievements;
	}

	/**
	 *
	 */
	@Embeddable
	public static final class UserIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7740660930809051850L;
		private final UUID identifier;

		/**
		 *
		 */
		UserIdentifier() {
			this(UUID.randomUUID());
		}

		/**
		 *
		 * @param identifier
		 */
		UserIdentifier(UUID identifier) {
			this.identifier = identifier;
		}

		/**
		 *
		 * @return
		 */
		@Override
		public int hashCode() {

			final int prime = 71;
			int result = 1;

			result = prime * result + (identifier == null ? 0 : identifier.hashCode());

			return result;
		}

		/**
		 *
		 * @param obj
		 * @return
		 */
		@Override
		public boolean equals(Object obj) {

			if (obj == this) {
				return true;
			}

			if (!(obj instanceof UserIdentifier that)) {
				return false;
			}

			return this.identifier.equals(that.identifier);
		}
	}
}

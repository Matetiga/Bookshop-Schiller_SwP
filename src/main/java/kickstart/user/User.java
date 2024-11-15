package kickstart.user;

import jakarta.persistence.*;
import kickstart.user.User.UserIdentifier;
import java.io.Serializable;
import java.util.UUID;
import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class User extends AbstractAggregateRoot<UserIdentifier> {
	private @EmbeddedId UserIdentifier id = new UserIdentifier();
	private String address;

	private @OneToOne UserAccount userAccount;
	

	public String getRole(){
		return "User";
	}

	@SuppressWarnings("unused")
	private User() {}

	public User(UserAccount userAccount, String address) {
		this.userAccount = userAccount;
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	@Override
	public UserIdentifier getId() {
		return id;
	}

	public Role getHighestRole(){
		if (this.getUserAccount().hasRole(Role.of("ADMIN"))) return Role.of("ADMIN");
		if (this.getUserAccount().hasRole((Role.of("EMPLOYEE")))) return Role.of("EMPLOYEE");

		return Role.of("CUSTOMER");
	}



	@Embeddable
	public static final class UserIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7740660930809051850L;
		private final UUID identifier;


		UserIdentifier() {
			this(UUID.randomUUID());
		}
		UserIdentifier(UUID identifier) {
			this.identifier = identifier;
		}

		@Override
		public int hashCode() {

			final int prime = 71;
			int result = 1;

			result = prime * result + (identifier == null ? 0 : identifier.hashCode());

			return result;
		}


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

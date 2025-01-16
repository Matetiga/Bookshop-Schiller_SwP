package kickstart.user;

import kickstart.user.User.UserIdentifier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;


public interface UserRepository extends CrudRepository<User, UserIdentifier> {
	/**
	 *
	 * @return
	 */
	@Override
	Streamable<User> findAll();
}

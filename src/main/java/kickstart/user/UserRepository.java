package kickstart.user;

import kickstart.user.User.UserIdentifier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;


interface UserRepository extends CrudRepository<User, UserIdentifier> {

	@Override
	Streamable<User> findAll();
}

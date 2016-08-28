package se.aaasen.jpa.repository;

import java.util.List;

import se.aaasen.jpa.model.User;


public interface UserRepository extends CRUDRepository<User>
{
	public User getUserByUsername(String username);

	public List<User> getAll();
}

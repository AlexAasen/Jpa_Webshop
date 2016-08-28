package se.aaasen.jpa.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.User.UserStatus;
import se.aaasen.jpa.repository.JpaUserRepository;
import se.aaasen.jpa.repository.UserRepository;

public class TestJpaUserRepository
{
	private static EntityManagerFactory factory;
	private static UserRepository userRepository;

	private static final String username1 = "username1";
	private static final String username2 = "username2";
	private static final String firstName = "firstName";
	private static final String lastName = "lastName";
	private static final String password = "p455Word!?";
	private static final UserStatus userStatus = UserStatus.ADMIN;

	private static User user1, user2;

	@BeforeClass
	public static void initializeTests() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("thePersistenceUnit");
		userRepository = new JpaUserRepository(factory);

		user1 = new User(username1, firstName, lastName, password, userStatus);
		user2 = new User(username2, firstName, lastName, password, userStatus);

		userRepository.createOrUpdate(user1); // 1L
		userRepository.createOrUpdate(user2); // 2L
	}

	@Before
	public void setUpMethods() //Make sure that any changes depending on the order of the tests are updated in the variables.
	{
		user1 = userRepository.findById(1L);
	}

	// -----------TEST USERS
	// Be aware, H2 assigns Id in add-order, first add = 1L, second = 2L etc.
	// Count is added as comments by respective add method as reference.
	@Test
	public void getUserById()
	{
		User testUser1 = userRepository.findById(1L);
		assertThat(testUser1, is(equalTo(user1)));
	}

	@Test
	public void getAllUsers()
	{
		List<User> users = userRepository.getAll();

		assertThat(users.size(), is(2));
		assertThat(users.get(0), is(equalTo(user1)));
		assertThat(users.get(1), is(equalTo(user2)));
	}

	@Test
	public void userIsAddedAndUserIsCollectedByUserName()
	{
		User testUser1 = userRepository.getUserByUsername(username1);
		assertThat(testUser1, is(equalTo(user1)));
	}

	@Test
	public void userIsUpdated()
	{
		User updated = userRepository.findById(1L);
		updated.setFirstName("YodaIsReal"); 
		userRepository.createOrUpdate(updated);

		user1 = userRepository.findById(1L);
		assertThat(user1, is(updated));
	}

	@Test
	public void userStatusIsUpdated()
	{
		User updated = userRepository.findById(1L);
		updated.setUserStatus(UserStatus.USER);
		userRepository.createOrUpdate(updated);

		user1 = userRepository.findById(1L);
		assertThat(user1, is(updated));
	}
}
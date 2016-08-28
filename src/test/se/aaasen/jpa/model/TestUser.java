package se.aaasen.jpa.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.User.UserStatus;

public class TestUser
{
	private final String username = "username";
	private final String firstName = "firstName";
	private final String lastName = "lastName";
	private final String password = "p455Word!?";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void twoUsersWithSameParametersShouldBeEqual()
	{
		User user1 = new User(username, firstName, lastName, password, UserStatus.ADMIN);
		User user2 = new User(username, firstName, lastName, password, UserStatus.ADMIN);

		assertThat(user1, equalTo(user2));
	}

	@Test
	public void twoUsersThatAreEqualShoulHaveTheSameHashCode()
	{
		User user1 = new User(username, firstName, lastName, password, UserStatus.ADMIN);
		User user2 = new User(username, firstName, lastName, password, UserStatus.ADMIN);

		assertThat(user1, equalTo(user2));
		assertThat(user1.hashCode(), equalTo(user2.hashCode()));
	}

	@Test
	public void usernameCannotBeNull()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("username cannot be null");

		new User(null, firstName, lastName, password, UserStatus.ADMIN);
	}
	
	@Test
	public void usernameShouldBeCorrectLenght()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Username must consist of 5-30 characters");

		new User("Yoda", firstName, lastName, password, UserStatus.ADMIN);
	}
	
	@Test
	public void passwordCannotBeNull()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("password cannot be null");

		new User(username, firstName, lastName, null, UserStatus.ADMIN);
	}

	@Test
	public void passwordShouldBeCorrectLenght()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Password is required to be atleast 4 characters");

		new User(username, firstName, lastName, "hej", UserStatus.ADMIN);
	}

	@Test
	public void passwordNeedsToContainOneSpecialCharacter()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Atleast one special character is required in your password");

		new User(username, firstName, lastName, "Hej12", UserStatus.ADMIN);
	}

	@Test
	public void passwordNeedsToContainAtleastTwoNumbers()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Atleast two numbers are required in your password");

		new User(username, firstName, lastName, "Hej1!?", UserStatus.ADMIN);
	}

	@Test
	public void passwordNeedsToContainAtleastOneUpperCase()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Atleast one UpperCase-Letter is required in your password");

		new User(username, firstName, lastName, "hej123!?", UserStatus.ADMIN);
	}
}
package se.aaasen.jpa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries(value = { @NamedQuery(name = "User.GetAll", query = "select u from User u"),
		@NamedQuery(name = "User.ByUsername", query = "select u from User u where u.username = :username") })
public class User extends AbstractEntity
{
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	@OneToMany(mappedBy = "user")
	private Collection<Order> orders;

	public enum UserStatus
	{
		ADMIN, USER, ACCOUNT_CLOSED;
	}

	protected User()
	{
	}

	public User(String username, String firstName, String lastName, String password, UserStatus userStatus)
	{
		isUserNameValid(username);
		isPassWordValid(password);
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.userStatus = userStatus;
	}

	private void isUserNameValid(String username)
	{
		if (username == null)
		{
			throw new NullPointerException("username cannot be null");
		}
		if ((username.length() < 5) || (username.length() > 30))
		{
			throw new IllegalArgumentException("Username must consist of 5-30 characters");
		}
	}

	private void isPassWordValid(String password)
	{
		if (password == null)
		{
			throw new NullPointerException("password cannot be null");
		}
		if ((!(password.length() >= 4)) || password == null)
		{
			throw new IllegalArgumentException("Password is required to be atleast 4 characters");
		}
		// Method checks if the incoming password contains the specified
		// characters
		// [A-Z] Sets parameters to Uppercase letters a-z. + means that one of
		// them
		// Should appear ONE or MORE times.
		String atleastOneUpperCase = "[A-Z]+";
		// [0-9] sets parameters to decimals 0-9. {2,} means that one of them
		// should appear
		// TWO or MORE times.
		String atleastTwoNumbers = "[0-9]{2,}";
		// Sets the parameter to a bunch of special characters, one of which
		// needs to appear
		// atleast ONCE.
		String atleastOneSpecial = "[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+";

		Pattern p = Pattern.compile(atleastOneUpperCase);
		Matcher m = p.matcher(password);
		boolean characterFound = m.find();

		if (characterFound)
		{
			p = Pattern.compile(atleastTwoNumbers);
			m = p.matcher(password);
			characterFound = m.find();

			if (characterFound)
			{
				p = Pattern.compile(atleastOneSpecial);
				m = p.matcher(password);
				characterFound = m.find();

				if (!characterFound)
				{
					throw new IllegalArgumentException("Atleast one special character is required in your password");
				}
			}
			else
			{
				throw new IllegalArgumentException("Atleast two numbers are required in your password");
			}
		}
		else
		{
			throw new IllegalArgumentException("Atleast one UpperCase-Letter is required in your password");
		}
	}

	public List<Order> getOrder()
	{
		return new ArrayList<>(this.orders);
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public UserStatus getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus)
	{
		this.userStatus = userStatus;
	}

	public void updateValues(User user)
	{
		isUserNameValid(user.getUsername());
		isPassWordValid(user.getPassword());
		this.firstName = user.firstName;
		this.lastName = user.lastName;
		this.password = user.password;
		this.userStatus = user.userStatus;
	}

	@Override
	public int hashCode()
	{
		int result = 11;
		result += 37 * this.username.hashCode();
		result += 37 * this.firstName.hashCode();
		result += 37 * this.lastName.hashCode();
		result += 37 * this.password.hashCode();
		result += 37 * this.userStatus.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof User)
		{
			User otherUser = (User) other;
			return this.username.equals(otherUser.username) && this.firstName.equals(otherUser.firstName) && this.lastName.equals(otherUser.lastName)
					&& this.password.equals(otherUser.password) && this.userStatus.equals(otherUser.userStatus);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return getId() + " " + getUserStatus() + " " + this.username + " " + this.firstName + " " + this.lastName;
	}
}
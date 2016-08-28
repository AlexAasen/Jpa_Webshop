package se.aaasen.jpa.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import se.aaasen.jpa.model.Order;
import se.aaasen.jpa.model.Product;
import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.Order.OrderStatus;
import se.aaasen.jpa.model.Product.ProductStatus;
import se.aaasen.jpa.model.User.UserStatus;

public class TestOrder
{
	private final String username = "username1";
	private final String firstName = "firstName";
	private final String lastName = "lastName";
	private final String password = "p455Word!?";
	private final UserStatus userStatus = UserStatus.USER;
	
	private final int itemNumber = 1001;
	private final String productName = "Jeans";
	private final double productPrice = 499.95;
	private final int amount = 1;
	private final ProductStatus productStatus = ProductStatus.IN_STOCK;
	
	private User user;
	private Product product;

	@Before
	public void setup()
	{
		user = new User(username, firstName, lastName, password, userStatus);
		product = new Product(itemNumber, productName, productPrice, productStatus);
	}
	
	@Test
	public void productsAreAddedIntoOrdersOrderRows()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);
		
		boolean productExists = false;
		for (Map.Entry<Product, Integer> entry : order1.getCart().entrySet())
		{
			if(entry.getKey().equals(product) && (entry.getValue() == amount))
			{
				productExists = true;
			}
		}
		assertTrue(productExists);
	}
	
	@Test
	public void productsCanBeRemovedFromAnOrdersOrderRows()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);
		order1.removeProduct(product);
		
		boolean productExists = false;
		for (Map.Entry<Product, Integer> entry : order1.getCart().entrySet())
		{
			if(entry.getKey().equals(product) && (entry.getValue() == amount))
			{
				productExists = true;
			}
		}
		assertFalse(productExists);
	}
	
	@Test
	public void productsCanBeUpdatedInAnOrdersOrderRows()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);
		order1.updateProductAmount(product, 3);
		
		boolean productIsUpdated = false;
		for (Map.Entry<Product, Integer> entry : order1.getCart().entrySet())
		{
			if(entry.getKey().equals(product) && (entry.getValue() == 3))
			{
				productIsUpdated = true;
			}
		}
		assertTrue(productIsUpdated);
	}
	
	@Test
	public void addingTheSameProductAddsToTheExistingOrderRow()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);
		order1.addProduct(product, 4);
		
		boolean productIsUpdated = false;
		for (Map.Entry<Product, Integer> entry : order1.getCart().entrySet())
		{
			if(entry.getKey().equals(product) && (entry.getValue() == (amount + 4)))
			{
				productIsUpdated = true;
			}
		}
		assertTrue(productIsUpdated);
		
		for (Map.Entry<Product, Integer> entry : order1.getCart().entrySet())
		{
			if(entry.getKey().equals(product) && (entry.getValue() == amount))
			{
				productIsUpdated = false;
			}
		}
		assertTrue(productIsUpdated);
	}

	@Test
	public void twoOrdersWithTheSameValuesShouldBeEqualAndProduceSameHashCode()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);
		order1.setOrderStatus(OrderStatus.PLACED);
		
		Order order2 = new Order(user);
		order2.addProduct(product, amount);
		order2.setOrderStatus(OrderStatus.PLACED);

		assertThat(order1, is(equalTo(order2)));
		assertThat(order1.hashCode(), equalTo(order2.hashCode()));
	}

	@Test
	public void OrderCalculatesTotalPriceOfTheOrder()
	{
		Order order1 = new Order(user);
		order1.addProduct(product, amount);

		Order order2 = new Order(user);
		order2.addProduct(product, 4);
		
		assertThat((order1.getOrderValue()), is(product.getPrice() * amount));
		assertThat((order2.getOrderValue()), is(product.getPrice() * 4));
	}
}


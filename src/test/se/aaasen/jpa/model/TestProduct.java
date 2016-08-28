package se.aaasen.jpa.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import se.aaasen.jpa.model.Product;
import se.aaasen.jpa.model.Product.ProductStatus;

public class TestProduct
{
	private final int itemNumber = 1001;
	private final String productName = "Jeans";
	private final double productPrice = 499.99;
	private final ProductStatus productStatus = ProductStatus.IN_STOCK;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void twoProductsWithTheSameParametersShouldBeEqual()
	{
		Product product1 = new Product(itemNumber, productName, productPrice, productStatus);
		Product product2 = new Product(itemNumber, productName, productPrice, productStatus);

		assertThat(product1, equalTo(product2));
	}
	
	@Test
	public void twoProductsThatAreEqualShouldProduceTheSameHashCode()
	{
		Product product1 = new Product(itemNumber, productName, productPrice, productStatus);
		Product product2 = new Product(itemNumber, productName, productPrice, productStatus);

		assertThat(product1, equalTo(product2));
		assertThat(product1.hashCode(), equalTo(product2.hashCode()));
	}

	@Test
	public void productMustHaveAName()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("You have to give your product a name");

		new Product(itemNumber, null, productPrice, productStatus);
	}

	@Test
	public void productMustHaveAPriceGreaterThanZero()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("You have to give your product a price");

		new Product(itemNumber, productName, 0, productStatus);
	}
	
	@Test
	public void productCantBeOver50000()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("The price for a product can't be above 50000kr");
		
		new Product(itemNumber, productName, 50001, productStatus);
	}
}

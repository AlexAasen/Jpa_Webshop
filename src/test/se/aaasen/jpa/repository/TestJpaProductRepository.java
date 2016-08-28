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

import se.aaasen.jpa.model.Product;
import se.aaasen.jpa.model.Product.ProductStatus;
import se.aaasen.jpa.repository.JpaProductRepository;
import se.aaasen.jpa.repository.ProductRepository;

public class TestJpaProductRepository
{
	private static EntityManagerFactory factory;
	private static ProductRepository productRepository;

	private static final int itemNumber1 = 1001;
	private static final int itemNumber2 = 1002;
	private static final int itemNumber3 = 1003;
	private static final String productName1 = "Jeans";
	private static final String productName2 = "Sjal";
	private static final String productName3 = "Trosor";
	private static final double productPrice = 499.99;
	private static final ProductStatus productStatus = ProductStatus.IN_STOCK;

	private static Product product1, product2, product3;

	@BeforeClass
	public static void initializeTests() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("thePersistenceUnit");
		productRepository = new JpaProductRepository(factory);

		product1 = new Product(itemNumber1, productName1, productPrice, productStatus);
		product2 = new Product(itemNumber2, productName2, productPrice, productStatus);
		product3 = new Product(itemNumber3, productName3, productPrice, productStatus);

		productRepository.createOrUpdate(product1); // 1L
		productRepository.createOrUpdate(product2); // 2L
		productRepository.createOrUpdate(product3); // 3L
	}

	@Before
	public void setUpMethods() //Make sure that any changes depending on the order of the tests are updated in the variables.
	{
		product1 = productRepository.findById(1L);
	}

	// --------- TEST PRODUCTS
	@Test
	public void getProductById()
	{
		Product testProduct1 = productRepository.findById(1L);
		assertThat(testProduct1, is(equalTo(product1)));
	}

	@Test
	public void getAllProducts()
	{
		List<Product> products = productRepository.getAll();

		assertThat(products.size(), is(3));
		assertThat(products.get(0), is(equalTo(product1)));
		assertThat(products.get(1), is(equalTo(product2)));
		assertThat(products.get(2), is(equalTo(product3)));
	}

	@Test
	public void productIsAddedAndProductIsCollectedByProductName()
	{
		Product testProduct1 = productRepository.getProductByProductName(productName1);
		assertThat(testProduct1, is(equalTo(product1)));
	}
	
	@Test
	public void productIsCollectedByItemNumber()
	{
		Product testProduct1 = productRepository.getProductByItemNumber(itemNumber1);
		assertThat(testProduct1, is(equalTo(product1)));
	}

	@Test
	public void productIsUpdated()
	{
		Product updated = productRepository.findById(1L);
		updated.setPrice(49.99);
		productRepository.createOrUpdate(updated);

		product1 = productRepository.findById(1L);
		assertThat(product1, is(updated));
	}

	@Test
	public void productStatusIsUpdated()
	{
		Product updated = productRepository.findById(1L);
		updated.setProductStatus(ProductStatus.MANUFACTURER_SENDING_TO_PEDIWEAR);
		productRepository.createOrUpdate(updated);
		
		product1 = productRepository.findById(1L);
		assertThat(product1, is(updated));
	}
}
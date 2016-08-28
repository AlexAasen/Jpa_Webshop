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

import se.aaasen.jpa.model.Order;
import se.aaasen.jpa.model.Product;
import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.Order.OrderStatus;
import se.aaasen.jpa.model.Product.ProductStatus;
import se.aaasen.jpa.model.User.UserStatus;
import se.aaasen.jpa.repository.JpaOrderRepository;
import se.aaasen.jpa.repository.JpaProductRepository;
import se.aaasen.jpa.repository.JpaUserRepository;
import se.aaasen.jpa.repository.OrderRepository;
import se.aaasen.jpa.repository.ProductRepository;
import se.aaasen.jpa.repository.UserRepository;

public class TestJpaOrderRepository
{
	private static EntityManagerFactory factory;
	private static UserRepository userRepository;
	private static ProductRepository productRepository;
	private static OrderRepository orderRepository;

	private static final String username1 = "username1";
	private static final String username2 = "username2";
	private static final String firstName = "firstName";
	private static final String lastName = "lastName";
	private static final String password = "p455Word!?";
	private static final UserStatus userStatus = UserStatus.ADMIN;

	private static final int itemNumber1 = 1001;
	private static final String productName1 = "Jeans";
	private static final double productPrice = 499.99;
	private static final ProductStatus productStatus = ProductStatus.IN_STOCK;

	private static User user1, user2;
	private static Product product1;
	private static Order order1, order2, order3;
	
	@BeforeClass
	public static void initializeTests() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("thePersistenceUnit");
		userRepository = new JpaUserRepository(factory);
		productRepository = new JpaProductRepository(factory);
		orderRepository = new JpaOrderRepository(factory);
	
		user1 = new User(username1, firstName, lastName, password, userStatus);
		user2 = new User(username2, firstName, lastName, password, userStatus);
		product1 = new Product(itemNumber1, productName1, productPrice, productStatus);
	
		order1 = new Order(user1);
		order2 = new Order(user1);
		order3 = new Order(user2);
	
		order1.addProduct(product1, 2);
		order2.addProduct(product1, 3);
		order3.addProduct(product1, 1);
		
		order1.setOrderStatus(OrderStatus.PLACED);
		order2.setOrderStatus(OrderStatus.PAYED);
		order3.setOrderStatus(OrderStatus.PLACED);
	
		userRepository.createOrUpdate(user1); // 1L
		userRepository.createOrUpdate(user2); // 2L
	
		productRepository.createOrUpdate(product1); // 3L
	
		orderRepository.createOrUpdate(order1); // 4L
		orderRepository.createOrUpdate(order2); // 5L
		orderRepository.createOrUpdate(order3); // 6L
	}
	
	@Before
	public void setUpMethods() //Make sure that any changes depending on the order of the tests are updated in the variables.
	{
		user1 = userRepository.findById(1L);
		product1 = productRepository.findById(3L);
		order1 = orderRepository.findById(4L);
	}

//---------- TEST ORDERS
	@Test
	public void getOrderById()
	{
		Order testOrder1 = orderRepository.findById(4L);
		assertThat(testOrder1, is(equalTo(order1)));
	}

	@Test
	public void getAllOrders()
	{
		List<Order> orders = orderRepository.getAll();

		assertThat(orders.size(), is(3));
		assertThat(orders.get(0), is(equalTo(order1)));
		assertThat(orders.get(1), is(equalTo(order2)));
		assertThat(orders.get(2), is(equalTo(order3)));
	}

	@Test
	public void aSpecificUsersOrdersAreReturned()
	{
		List<Order> testOrders = orderRepository.getOrdersByUser(user1);

		assertThat(testOrders.size(), is(2));
		assertThat(testOrders.get(0), is(equalTo(order1)));
		assertThat(testOrders.get(1), is(equalTo(order2)));
	}

	@Test
	public void ordersAreReturnedByStatus()
	{
		List<Order> testOrders = orderRepository.getOrderByStatus(OrderStatus.PLACED);
		
		assertThat(testOrders.size(), is(2));
		assertThat(testOrders.get(0).getOrderStatus(), is(OrderStatus.PLACED));
		assertThat(testOrders.get(1).getOrderStatus(), is(OrderStatus.PLACED));
	}
	
	@Test
	public void ordersAreReturnedByMinimumValue()
	{
		List<Order> testOrders = orderRepository.getOrdersByMinimumValue(100);
		
		assertThat(testOrders.size(), is(3));
		assertTrue(testOrders.get(0).getOrderValue() > 100);
		assertTrue(testOrders.get(1).getOrderValue() > 100);
		assertTrue(testOrders.get(2).getOrderValue() > 100);
	}
	
	@Test
	public void orderIsUpdated()
	{
		Order updated = orderRepository.findById(4L);
		updated.updateProductAmount(product1, 5);
		orderRepository.createOrUpdate(updated);
		
		order1 = orderRepository.findById(4L);
		assertThat(order1, is(updated));
	}
	
	@Test
	public void orderStatusIsUpdated()
	{
		Order updated = orderRepository.findById(4L);
		updated.setOrderStatus(OrderStatus.PAYED);
		orderRepository.createOrUpdate(updated);

		order1 = orderRepository.findById(4L);
		assertThat(order1, is(updated));
	}
}

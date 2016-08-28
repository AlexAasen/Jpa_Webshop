package se.aaasen.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.aaasen.jpa.ECommerceManager;
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

public class TestECommerceManager
{
	private static EntityManagerFactory factory;
	private static UserRepository userRepository;
	private static ProductRepository productRepository;
	private static OrderRepository orderRepository;
	private static ECommerceManager eCommerceManager;

	private static final String username1 = "username1";
	private static final String username2 = "username2";
	private static final String firstName = "firstName";
	private static final String lastName = "lastName";
	private static final String password = "p455Word!?";
	private static final UserStatus userStatus = UserStatus.ADMIN;

	private static final int itemNumber1 = 1001;
	private static final int itemNumber2 = 1002;
	private static final int itemNumber3 = 1003;
	private static final String productName1 = "Jeans";
	private static final String productName2 = "Sjal";
	private static final String productName3 = "Trosor";
	private static final double productPrice = 499.99;
	private static final ProductStatus productStatus = ProductStatus.IN_STOCK;

	private static User user1, user2;
	private static Product product1, product2, product3;
	private static Order order1, order2;

	@BeforeClass
	public static void initializeTests() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("thePersistenceUnit");
		userRepository = new JpaUserRepository(factory);
		productRepository = new JpaProductRepository(factory);
		orderRepository = new JpaOrderRepository(factory);
		eCommerceManager = new ECommerceManager(productRepository, userRepository, orderRepository);

		user1 = new User(username1, firstName, lastName, password, userStatus);
		user2 = new User(username2, firstName, lastName, password, userStatus);

		product1 = new Product(itemNumber1, productName1, productPrice, productStatus);
		product2 = new Product(itemNumber2, productName2, productPrice, productStatus);
		product3 = new Product(itemNumber3, productName3, productPrice, productStatus);

		order1 = new Order(user1);
		order2 = new Order(user1);

		order1.addProduct(product1, 2);
		order2.addProduct(product1, 3);

		eCommerceManager.addUser(user1); // 1L
		eCommerceManager.addUser(user2); // 2L

		eCommerceManager.addProduct(product1); // 3L
		eCommerceManager.addProduct(product2); // 4L
		eCommerceManager.addProduct(product3); // 5L

		eCommerceManager.placeOrder(order1); // 6L
		eCommerceManager.placeOrder(order2); // 7L
	}

	@Before
	public void setUpMethods() // Make sure that any changes depending on the order of the tests are updated in the variables.
	{
		user1 = eCommerceManager.getUserById(1L);
		product1 = eCommerceManager.getProductById(3L);
		order1 = eCommerceManager.getOrderById(6L);
	}

	// -----------TEST USERS
	// Be aware, H2 assigns Id in add-order, first add = 1L, second = 2L etc.
	// Count is added as comments by respective add method as reference.
	@Test
	public void getUserById()
	{
		User testUser1 = eCommerceManager.getUserById(1L);
		assertThat(testUser1, is(equalTo(user1)));
	}

	@Test
	public void getAllUsers()
	{
		List<User> users = eCommerceManager.getAllUsers();

		assertThat(users.size(), is(2));
		assertThat(users.get(0), is(equalTo(user1)));
		assertThat(users.get(1), is(equalTo(user2)));
	}

	@Test
	public void userIsAddedAndUserIsCollectedByUserName()
	{
		User testUser1 = eCommerceManager.getUserByUsername(username1);
		assertThat(testUser1, is(equalTo(user1)));
	}

	@Test
	public void userIsUpdated()
	{
		User updated = new User(username1, "YodaIsReal", lastName, password, userStatus);
		eCommerceManager.updateUser(1L, updated);

		user1 = eCommerceManager.getUserById(1L);
		assertThat(user1, is(updated));
	}

	@Test
	public void userStatusIsUpdated()
	{
		UserStatus updated = UserStatus.USER;
		eCommerceManager.updateStatus(1L, updated);

		user1 = eCommerceManager.getUserById(1L);
		assertThat(user1.getUserStatus(), is(updated));
	}

	// --------- TEST PRODUCTS
	@Test
	public void getProductById()
	{
		Product testProduct1 = eCommerceManager.getProductById(3L);
		assertThat(testProduct1, is(equalTo(product1)));
	}

	@Test
	public void getAllProducts()
	{
		List<Product> products = eCommerceManager.getAllProducts();

		assertThat(products.size(), is(3));
		assertThat(products.get(0), is(equalTo(product1)));
		assertThat(products.get(1), is(equalTo(product2)));
		assertThat(products.get(2), is(equalTo(product3)));
	}

	@Test
	public void productIsAddedAndProductIsCollectedByProductName()
	{
		Product testProduct1 = eCommerceManager.getProductByProductName(productName1);
		assertThat(testProduct1, is(equalTo(product1)));
	}

	@Test
	public void productIsCollectedByItemNumber()
	{
		Product testProduct1 = eCommerceManager.getProductByItemNumber(itemNumber1);
		assertThat(testProduct1, is(equalTo(product1)));
	}

	@Test
	public void productIsUpdated()
	{
		Product updated = new Product(itemNumber3, productName3, 49.99, productStatus);
		eCommerceManager.updateProduct(eCommerceManager.getProductByProductName(productName3).getId(), updated);

		product3 = eCommerceManager.getProductById(5L);
		assertThat(product3, is(updated));
	}

	@Test
	public void productStatusIsUpdated()
	{
		ProductStatus updated = ProductStatus.MANUFACTURER_SENDING_TO_PEDIWEAR;
		eCommerceManager.updateStatus(3L, updated);

		product1 = eCommerceManager.getProductById(3L);
		assertThat(product1.getProductStatus(), is(updated));
	}

	// ---------- TEST ORDERS
	@Test
	public void getOrderById()
	{
		Order testOrder1 = eCommerceManager.getOrderById(6L);
		assertThat(testOrder1, is(equalTo(order1)));
	}

	@Test
	public void getAllOrders()
	{
		List<Order> orders = eCommerceManager.getAllOrders();

		assertThat(orders.size(), is(2));
		assertThat(orders.get(0), is(equalTo(order1)));
		assertThat(orders.get(1), is(equalTo(order2)));
	}

	@Test
	public void aSpecificUsersOrdersAreReturned()
	{
		List<Order> testOrders = eCommerceManager.getOrdersByUser(user1);

		assertThat(testOrders.size(), is(2));
		assertThat(testOrders.get(0), is(equalTo(order1)));
		assertThat(testOrders.get(1), is(equalTo(order2)));
	}

	@Test
	public void orderIsUpdated()
	{
		Order updated = new Order(user1);
		updated.addProduct(product1, 5);
		eCommerceManager.updateOrder(6L, updated);

		order1 = eCommerceManager.getOrderById(6L);
		assertThat(order1, is(updated));
	}

	@Test
	public void orderStatusIsUpdated()
	{
		OrderStatus updated = OrderStatus.PAYED;
		eCommerceManager.updateStatus(6L, updated);

		order1 = eCommerceManager.getOrderById(6L);
		assertThat(order1.getOrderStatus(), is(updated));
	}

	@Test
	public void ordersAreReturnedByMinimumValue()
	{
		boolean valueGreaterThanMinimum;
		List<Order> testOrders = eCommerceManager.getOrdersByMinimumValue(100);

		for (Order order : testOrders)
		{
			double value = order.getOrderValue();
			valueGreaterThanMinimum = false;
			if (value > 100)
			{
				valueGreaterThanMinimum = true;
			}
			assertTrue(valueGreaterThanMinimum);
		}
	}

	@Test
	public void ordersAreReturnedByStatus()
	{
		boolean orderIsPlaced;
		List<Order> testOrders = eCommerceManager.getOrderByStatus(OrderStatus.PLACED);

		for (Order order : testOrders)
		{
			OrderStatus actualStatus = order.getOrderStatus();
			orderIsPlaced = false;
			if (actualStatus.equals(OrderStatus.PLACED))
			{
				orderIsPlaced = true;
			}
			assertTrue(orderIsPlaced);
		}
	}
}
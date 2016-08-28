package se.aaasen.jpa.repository;

import static java.util.function.Function.identity;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.aaasen.jpa.model.Order;
import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.Order.OrderStatus;

public final class JpaOrderRepository extends AbstractJpaRepository<Order>implements OrderRepository
{
	public JpaOrderRepository(EntityManagerFactory factory)
	{
		super(factory, Order.class);
	}

	@Override
	public List<Order> getOrdersByUser(User user)
	{
		return query("Orders.ByUser", query -> query.setParameter("userid", user));
	}

	@Override
	public List<Order> getOrdersByMinimumValue(double value)
	{
		return query("Orders.ByMinimumValue", query -> query.setParameter("orderValue", value));
	}

	@Override
	public List<Order> getOrderByStatus(OrderStatus orderStatus)
	{
		return query("Orders.ByStatus", query -> query.setParameter("status", orderStatus));
	}

	@Override
	public List<Order> getAll()
	{
		return query("Orders.GetAll", identity());
	}
}
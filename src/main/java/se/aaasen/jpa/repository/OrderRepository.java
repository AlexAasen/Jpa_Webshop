package se.aaasen.jpa.repository;

import java.util.List;

import se.aaasen.jpa.model.Order;
import se.aaasen.jpa.model.User;
import se.aaasen.jpa.model.Order.OrderStatus;

public interface OrderRepository extends CRUDRepository<Order>
{
	public List<Order> getOrdersByUser(User user);

	public List<Order> getOrdersByMinimumValue(double value);

	public List<Order> getOrderByStatus(OrderStatus orderStatus);

	public List<Order> getAll();
}

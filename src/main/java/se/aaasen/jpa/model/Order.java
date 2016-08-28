package se.aaasen.jpa.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.FetchType;

@Entity(name = "Orders")
@NamedQueries(value = {
		@NamedQuery(name = "Orders.GetAll", query = "select u from Orders u"),
		@NamedQuery(name = "Orders.ByStatus", query = "select u from Orders u where u.orderStatus = :status"),
		@NamedQuery(name = "Orders.ByMinimumValue", query = "select u from Orders u where u.orderValue >= :orderValue"),
		@NamedQuery(name = "Orders.ByUser", query = "select u from Orders u where u.user = :userid") })
public class Order extends AbstractEntity
{
	@Column
	private double orderValue;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable
	@Column(name = "Quantity")
	private Map<Product, Integer> orderRows;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	@ManyToOne
	private User user;

	public enum OrderStatus
	{
		PLACED, SHIPPED, PAYED, CANCELLED;
	}

	protected Order()
	{
	}

	public Order(User user)
	{
		this.user = user;
		orderRows = new HashMap<Product, Integer>();
	}

	public double getOrderValue()
	{
		return orderValue;
	}

	public Map<Product, Integer> getCart()
	{
		return orderRows;
	}

	public void setOrderValues(Order order)
	{
		this.orderRows = order.orderRows;
		this.orderValue = order.orderValue;
		this.orderStatus = order.orderStatus;
	}

	public void addProduct(Product product, int amount)
	{
		if (orderRows.containsKey(product))
		{
			int newAmount = (orderRows.get(product) + amount);
			updateProductAmount(product, newAmount);
		}
		else
		{
			orderRows.put(product, amount);
			calculateValue(product, amount);
		}
	}

	public void removeProduct(Product product)
	{
		int amount = orderRows.get(product);
		orderRows.remove(product);
		removeValue(product, amount);
	}

	public void updateProductAmount(Product product, int newAmount)
	{
		removeProduct(product);
		orderRows.put(product, newAmount);
		calculateValue(product, newAmount);
	}

	private void calculateValue(Product product, int amount)
	{
		orderValue += (product.getPrice() * amount);
	}

	private void removeValue(Product product, int amount)
	{
		orderValue -= (product.getPrice() * amount);
	}

	public void setOrderStatus(OrderStatus status)
	{
		orderStatus = status;
	}

	public OrderStatus getOrderStatus()
	{
		return orderStatus;
	}

	@Override
	public int hashCode()
	{
		int result = 11;
		result += 37 * orderRows.hashCode();
		result += 37 + orderValue;
		result += 37 * user.hashCode();
		result += 37 * orderStatus.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Order)
		{
			Order otherOrder = (Order) other;
			return this.orderRows.equals(otherOrder.orderRows) &&
					this.user.equals(otherOrder.user) &&
					this.orderValue == otherOrder.orderValue &&
					this.orderStatus.equals(otherOrder.orderStatus);
		}
		return false;
	}

	public String orderRowsToString()
	{
		String cart = "";

		for (Map.Entry<Product, Integer> entry : this.orderRows.entrySet())
		{
			cart += entry.getKey().cartToString() + " : " + entry.getValue() + "\n";
		}
		return cart;
	}

	@Override
	public String toString()
	{
		return "Order id: " + getId() + ", User: " + user.getUsername() + ", Cart: " + orderRowsToString() + ". Total " + orderValue + "kr " + orderStatus.toString();
	}
}

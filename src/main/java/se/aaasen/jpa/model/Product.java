package se.aaasen.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = { @NamedQuery(name = "Product.GetAll", query = "select p from Product p"),
		@NamedQuery(name = "Product.ByProductName", query = "select p from Product p where p.productName = :productName"),
		@NamedQuery(name = "Product.ByItemNumber", query = "select p from Product p where p.itemNumber = :itemNumber") })
public class Product extends AbstractEntity
{
	@Column(nullable = false, unique = true)
	private int itemNumber;
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private double price;
	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;

	public enum ProductStatus
	{
		IN_STOCK, OUT_OF_STOCK, MANUFACTURER_SENDING_TO_PEDIWEAR, DISCONTINUED;
	}

	protected Product()
	{
	}

	public Product(int itemNumber, String productName, double price, ProductStatus productStatus)
	{
		isProductCorrect(productName, price);
		this.itemNumber = itemNumber;
		this.productName = productName;
		this.price = price;
		this.productStatus = productStatus;
	}

	private void isProductCorrect(String productName, double productPrice)
	{
		if ((productName == null) || (productName.trim().length() == 0))
		{
			throw new IllegalArgumentException("You have to give your product a name");
		}
		if ((productPrice == 0))
		{
			throw new IllegalArgumentException("You have to give your product a price");
		}
		if ((productPrice > 50000))
		{
			throw new IllegalArgumentException("The price for a product can't be above 50000kr");
		}
	}

	public int getItemNumber()
	{
		return this.itemNumber;
	}

	public void setItemNumber(int itemNumber)
	{
		this.itemNumber = itemNumber;
	}

	public String getProductName()
	{
		return this.productName;
	}

	public double getPrice()
	{
		return this.price;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public ProductStatus getProductStatus()
	{
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus)
	{
		this.productStatus = productStatus;
	}

	@Override
	public int hashCode()
	{
		int result = 11;
		result += 37 * this.itemNumber;
		result += 37 * this.price;
		result += 37 * this.productName.hashCode();
		result += 37 * this.productStatus.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Product)
		{
			Product otherProduct = (Product) other;
			return this.itemNumber == (otherProduct.itemNumber) &&
					this.price == (otherProduct.price) &&
					this.productName.equals(otherProduct.productName) &&
					this.productStatus.equals(otherProduct.productStatus);
		}
		return false;
	}

	public String cartToString()
	{
		return itemNumber + ": " + this.productName + " " + this.price + "kr ";
	}
	
	@Override
	public String toString()
	{
		return getId() + ", " + itemNumber + ", " + this.productName + " " + this.price + "kr " + this.productStatus;
	}

	public void updateValues(Product product)
	{
		isProductCorrect(product.getProductName(), product.getPrice());
		this.productName = product.productName;
		this.price = product.price;
		this.productStatus = product.productStatus;
	}
}
package se.aaasen.jpa.repository;

import static java.util.function.Function.identity;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.aaasen.jpa.model.Product;

public final class JpaProductRepository extends AbstractJpaRepository<Product>implements ProductRepository
{
	public JpaProductRepository(EntityManagerFactory factory)
	{
		super(factory, Product.class);
	}

	@Override
	public Product getProductByProductName(String productName)
	{
		return querySingle("Product.ByProductName", query -> query.setParameter("productName", productName));
	}

	@Override
	public List<Product> getAll()
	{
		return query("Product.GetAll", identity());
	}

	@Override
	public Product getProductByItemNumber(int itemNumber)
	{
		return querySingle("Product.ByItemNumber", query -> query.setParameter("itemNumber", itemNumber));
	}
}

package se.aaasen.jpa.repository;

import java.util.List;

import se.aaasen.jpa.model.Product;

public interface ProductRepository extends CRUDRepository<Product>
{
	public Product getProductByProductName(String productName);

	public Product getProductByItemNumber(int itemNumber);

	public List<Product> getAll();
}

package se.aaasen.jpa.repository;

import se.aaasen.jpa.model.AbstractEntity;

public interface CRUDRepository<E extends AbstractEntity>
{
	E createOrUpdate(E entity);

	E findById(Long id);
}

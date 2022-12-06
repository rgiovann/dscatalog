package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

// this annotation register this class as a component 
// that will be part of the dependency injection system

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	// ACID properties
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
		// Page already is an stream since Java 8.X, noo need to convert
		return list.map(p -> new ProductDTO(p));

	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Error. Id not found: " + id));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product entity = new Product();
		copyDtoToEntity(productDTO,entity);
		entity = repository.save(entity); // reposity.save() returns a reference to object saved in DB
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			
			Product proxyEntity = repository.getReferenceById(id);
			copyDtoToEntity(productDTO,proxyEntity);
			/*
			 * -- https://www.baeldung.com/jpa-entity-manager-get-reference --
			 * Surprisingly, the result of the running test method is still the same and 
			 * we see the SELECT query remains. As we can see, Hibernate does execute a 
			 * SELECT query when we use getReference() to update an entity field.
			 * Therefore, using the getReference() method does not avoid the extra SELECT
			 * query if we execute any setter of the entity proxy's fields.
			 */
			
			/*
			 *  -- https://vladmihalcea.com/jpa-persist-and-merge/ --
			 *  The save method serves no purpose. Even if we remove it, Hibernate will still
			 *  issue the UPDATE statement since the entity is managed and any state change 
			 *  is propagated as long as the currently running EntityManager is open.
			 * 
			 */
			
			proxyEntity = repository.save(proxyEntity);			
			return new ProductDTO(proxyEntity);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Error. Id not found : " + id);
		}
	}

	/*
	 * Persisting and deleting objects in JPA requires a transaction. That's why we should use 
	 * a @Transactional annotation when using these derived delete queries, 
	 * to make sure a transaction is running. 
	 * This is explained in detail in the ORM with Spring documentation.
	 * 
	 */
	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Error. Id not found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Error. Integrity violation: " + id);
		}

	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());	
		entity.getCategories().clear();
		for ( CategoryDTO catDTO : dto.getCategories())
		{
			 Category category = categoryRepository.getReferenceById(catDTO.getId());
			 entity.getCategories().add(category);
		}
	}
}

package com.devsuperior.dscatalog.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public interface ProductRepositoryBase {
	
	Page<Product>      findCustomized(List<Category> categories, String name, Pageable page);
	Optional<Product>  findById(Long id);
	Product save(Product entity);
	void deleteById(Long id);
}

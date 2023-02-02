package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Profile("test")
public interface ProductRepositoryTest extends ProductRepositoryBase,JpaRepository<Product, Long> {

	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats "
		  	 + "WHERE (:categories IS NULL OR cats IN :categories) AND "
		  	 + "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%' )))")
	Page<Product> findCustomized(List<Category> categories, String name, Pageable page);

}


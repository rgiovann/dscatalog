package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

// this annotation register this class as a component 
// that will be part of the dependency injection system

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	// ACID properties
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		return list.stream().map(p -> new CategoryDTO(p)).collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {		
		return new CategoryDTO (repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find requested category.")));
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category entity = new Category();
		entity.setName(categoryDTO.getName());
		entity = repository.save(entity);       // reposity.save() returns a reference to object saved in DB
		return new CategoryDTO(entity);
	}
	

}

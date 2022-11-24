package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

// this annotation register this class as a component 
// that will be part of the dependency injection system

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	// ACID properties
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return repository.findAll();
		
	}
}

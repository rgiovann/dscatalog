package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {
//	public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {

public static Product createProduct() {
	Product product = new Product(1L,"This is a cellphone","A very good phone. Indeed a very good but no sheap cellphone", 90.5, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/Big_picture_of_cellphone.jpg",Instant.parse( "2020-07-13T20:50:07Z"));
	product.getCategories().add(createCategory());
	return product;
}


public static ProductDTO createProductDTO() {
	Product product = createProduct();
	return new ProductDTO(product, product.getCategories());
}


public static Category createCategory() {
	Category category = new Category(2L,"Electronics");
	return category;
}

public static long getExistingCategoryId() {
	return 2L;
}

public static long getExistingProductId() {
	return 1L;
}


}

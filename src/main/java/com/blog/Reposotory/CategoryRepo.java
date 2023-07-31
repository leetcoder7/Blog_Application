package com.blog.Reposotory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.Category;

//here we send the data to database and perform db operation
public interface CategoryRepo extends JpaRepository<Category, Integer> {

}

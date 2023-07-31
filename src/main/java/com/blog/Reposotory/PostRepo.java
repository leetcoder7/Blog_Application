package com.blog.Reposotory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	
	List<Post> findByUser(User user); //these are the custom finder method we will use it to get post
	List<Post> findByCategory(Category category);
	
	//for searching
	//if we want to search content wise then change Title to Content
//	List<Post> findByTitleContaining(String title);
	
	//for the error that we were getting while searching for second time
	@Query("select p from Post p where p.title like :key")
	List<Post> searchByTitle(@Param("key") String title);
}
//ctrl + 1 + enter to get auto variable
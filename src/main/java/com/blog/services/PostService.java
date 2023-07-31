package com.blog.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.Reposotory.CategoryRepo;
import com.blog.Reposotory.PostRepo;
import com.blog.Reposotory.UserRepo;
import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

@Service
public class PostService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	//create
	
	public PostDto createPost(PostDto postDto,Integer userId, Integer categoryId) {
		
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "User id ", userId));
		
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id ", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		
		return this.modelMapper.map(newPost, PostDto.class);
	}
	
	//update
	
	public PostDto updatePost(PostDto postDto, Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "post Id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedpost = this.postRepo.save(post);
		return modelMapper.map(updatedpost, PostDto.class);
	}
	
	//delete
	
	public void deletePost(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "post Id", postId));
		this.postRepo.delete(post);
	}
	
	//get all posts
	
	//we changed the returned type List<PostDto> to PostResponse for dynamic sorting we took sortDir
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy,String sortDir){
		
		/*pagination
		we will take size & number dynamically
		int pageSize = 5;
		int pageNumber = 1;
				
		in method we passed sortby(title) it will come Sort.by(sortBy) here so 
		in the pagepost the data we will ger it will sort Sort.by(sortBy) here*/
		
		//for dynamic sorting
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		/*Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else {
			sort = Sort.by(sortBy).descending();
		}*/
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);//Sort.by(sortBy).descending()
		/*so we need Pageable object which we will get from PageRequest.of()
		then when we will pass Pageable object in findAll() it will return Page<Post> 
		then by using getContent() method we will get all the post*/
		
		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allPosts = pagePost.getContent();
		
		List<PostDto> postDtos = allPosts.stream().map((post)-> 
		           this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalelements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());;
		
		return postResponse;
	}
	
	//get single post
	
	public PostDto getPostById(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()-> 
		                                new ResourceNotFoundException("Post", "post Id", postId));
		
		return this.modelMapper.map(post, PostDto.class);
	}
	
	//get all post by category
	
	public List<PostDto> getPostByCategory(Integer categoryId){
		//so the id we are passing in method it will get the category based on id from database
		//and by using postRepo and findByCategory() to get all posts
		//then by using stream api then by map 
		//we are converting all posts to postDto then we collect it as a list List<PostDto> and returning the method
		
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "category Id", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		
		List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}
	
	//get all posts by user
	
	public List<PostDto> getPostByUser(Integer userId){
		
		
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "user Id", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		
		List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	
	}
	
	//search posts
	
	public List<PostDto> searchPosts(String keyword){
		
		List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");//"%"+keyword+"%" it will make the same query which containing method was making 
		//here we are converting post to postDto
		List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}
}

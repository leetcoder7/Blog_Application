package com.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private Integer postId;
	
	private String title;
	
	private String content;
	
	private String imageName;
	
	private Date addedDate;
	
	
	//by using this we will get category & user while returning post
	
	private CategoryDto category;
	
	private UserDto user;
	
	//we will get all the comments when we get any post
	private Set<CommentDto> comments = new HashSet<>();
}

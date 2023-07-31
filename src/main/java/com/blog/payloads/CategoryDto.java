package com.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
	
	private Integer categoryId;
	
	@NotBlank
	@Size(min = 4, message =  "min category title size must be 4")
	private String categoryTitle;
	
	@NotBlank
	@Size(min = 10, message =  "min category desc size must be 10")
	private String categoryDesc;
}
//this is for data transfer
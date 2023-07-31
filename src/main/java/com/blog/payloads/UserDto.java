package com.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {//dto - data transfer object
	
	private int id;
	
	@NotEmpty
	@Size(min=4,message="Name must be min 4 characters or long")
	private String name;
	
	@Email(message="your email address is not valid")
	private String email;
	
	@NotEmpty
	@Size(min=3,max=10,message="password must be min of 3 and max of 10 characters")
	private String password;
	
	@NotNull
	private String about;
}

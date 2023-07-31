package com.blog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.exceptions.*;
import com.blog.Reposotory.UserRepo;
import com.blog.entities.User;
import com.blog.payloads.UserDto;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserDto createUser(UserDto userDto) {
		
		//here we are converting user to userdto
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}
	public UserDto updateUser(UserDto userdto, Integer userId) {
		
		User user = this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User"," id ",userId)));
		
		user.setName(userdto.getName());
		user.setEmail(userdto.getEmail());
		user.setPassword(userdto.getPassword());
		user.setAbout(userdto.getAbout());
		
		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
		return userDto1;
	}
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User"," id ",userId)));
		
		return this.userToDto(user);
	}
	public List<UserDto> getAllUsers(){
		
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		//we use it here stream apis of lambdas
		return userDtos;
	}
	public void deleteUser(Integer userId) {
		
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User"," id ",userId));
		this.userRepo.delete(user);
	}
	
	
	public User dtoToUser(UserDto userDto) {
		
		User user = this.modelMapper.map(userDto, User.class);
		
		
//		User user = new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setAbout(userDto.getAbout());
//		user.setPassword(userDto.getPassword());
		return user;
	}
	

	public UserDto userToDto(User user) {
		
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		
//		UserDto userDto = new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setAbout(user.getAbout());
//		userDto.setPassword(user.getPassword());
		return userDto;
		
	}
	
    public registerNewUser(UserDto userDto) {
    	
    	User user = this.modelMapper.map(userDto, User.class);
    	
    	/*before saving the user we have to do 2 things
    	 * 1.To deal with its password
    	 * 2.To deal with its roles
    	 */
    	 
    	//to encode the password
    	user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    	
    	//setting roles to new user
    	
    	
	}
}

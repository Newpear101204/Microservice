package com.lehoangtan.userservice;



import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.lehoangtan.userservice.controller.UserController;
import com.lehoangtan.userservice.data.User;
import com.lehoangtan.userservice.model.UserDTO;
import com.lehoangtan.userservice.service.UserService;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
	
	@Mock
	private UserService userService;
	
	
	@InjectMocks
	private UserController userController;
	
	private User user;
	private UserDTO userDTO;
	
	@BeforeEach
	public void setUp() {
		user = new User(1L,"dev@gmail.com","123456","employeeID");
		userDTO = new UserDTO(1L,"dev@gmail.com","123456","employeeID","token","refresh");
		ReflectionTestUtils.setField(userController, "userService", userService);
	}
	
	@Test
	void getAllUser() {
		List<User> users = new ArrayList<>();
		users.add(user);
		when(userService.getAllUser()).thenReturn(users);
		 Assertions.assertEquals(users,userController.getAllUser());
		
	}
	
	@Test
	void addUser() {
		when(userService.saveUser(userDTO)).thenReturn(userDTO);
		Assertions.assertEquals(userDTO,userController.addUser(userDTO));
	}
	

	 @Test
	    void login(){
	        when(userService.login(anyString(),anyString())).thenReturn(userDTO);
	        Assertions.assertEquals(userDTO,userController.login(userDTO));
	    }

}

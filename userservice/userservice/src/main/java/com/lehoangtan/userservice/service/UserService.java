package com.lehoangtan.userservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lehoangtan.userservice.data.User;
import com.lehoangtan.userservice.data.UserRepository;
import com.lehoangtan.userservice.model.UserDTO;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> getAllUser(){
		List<User> list = userRepository.findAll();		
		return list;
	}
	
	public UserDTO saveUser(UserDTO userDTO) {
		User user = new User();
		BeanUtils.copyProperties(userDTO, user);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userRepository.save(user);
		return userDTO;
	}
	
	
	public UserDTO login(String username, String password) {
		User user = userRepository.findByUsername(username);
		UserDTO userDTO = new UserDTO();
		if (user != null) {
			BeanUtils.copyProperties(user, userDTO);
			
			if (passwordEncoder.matches(password, userDTO.getPassword())) {
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				String accessToken = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+ (1 * 60 * 10000)))
						.sign(algorithm);
				String refreshtoken  = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+ (1 * 60 * 10000)))
						.sign(algorithm);
				userDTO.setRefreshtoken(refreshtoken);
				userDTO.setToken(accessToken);
			}
		}
		
		return userDTO;
	}

}

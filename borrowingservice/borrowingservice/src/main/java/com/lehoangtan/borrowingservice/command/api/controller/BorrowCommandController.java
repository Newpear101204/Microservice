package com.lehoangtan.borrowingservice.command.api.controller;

import java.util.Date;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.lehoangtan.borrowingservice.command.api.command.CreateBorrowCommand;
import com.lehoangtan.borrowingservice.command.api.command.UpdateBookReturnCommand;
import com.lehoangtan.borrowingservice.command.api.model.BorrowRequestModel;
import com.lehoangtan.borrowingservice.command.api.service.BorrowService;
import com.lehoangtan.commonservice.command.UpdateStatusBookCommand;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowCommandController {
	@Autowired
	private CommandGateway commandGateway;
	
	@Autowired
	private BorrowService service;
	
	@PostMapping
	public String addBookBorrowing(@RequestBody BorrowRequestModel model) {
		try {
			CreateBorrowCommand command = 
					new CreateBorrowCommand(model.getBookId(), model.getEmployeeId(), new Date(),UUID.randomUUID().toString());
				commandGateway.sendAndWait(command);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "Book borrowing added";
	}
	
	@PutMapping
	public String returnBookBorrowing ( @RequestBody BorrowRequestModel model) {
		try {
			String id = service.findIdBorrowing(model.getEmployeeId(), model.getBookId());
			UpdateBookReturnCommand command = new UpdateBookReturnCommand(id, model.getBookId(), model.getEmployeeId(), new Date());
			commandGateway.sendAndWait(command);	
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		return "Book returned";
	}

}

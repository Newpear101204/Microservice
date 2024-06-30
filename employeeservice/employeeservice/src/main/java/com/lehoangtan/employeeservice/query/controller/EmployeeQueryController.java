package com.lehoangtan.employeeservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lehoangtan.employeeservice.query.model.EmployeeResponseModel;
import com.lehoangtan.employeeservice.query.queries.GetAllEmployeesQuery;
import com.lehoangtan.employeeservice.query.queries.GetEmployeeQuery;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeQueryController {

	@Autowired 
	private QueryGateway queryGateway;
	
	@GetMapping("/{employeeId}")
    public EmployeeResponseModel getEmployeeDetail(@PathVariable String employeeId) {
        GetEmployeeQuery getEmployeesQuery = new GetEmployeeQuery();
        getEmployeesQuery.setEmployeeId(employeeId);

        EmployeeResponseModel employeeReponseModel =
        queryGateway.query(getEmployeesQuery,
                ResponseTypes.instanceOf(EmployeeResponseModel.class))
                .join();

        return employeeReponseModel;
    }
	@GetMapping
	public List<EmployeeResponseModel> getAllEmployee(){
		List<EmployeeResponseModel> list = queryGateway.query(new GetAllEmployeesQuery(), ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class))
				.join();
		return list;
	}
	
}
package com.lehoangtan.employeeservice.query.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lehoangtan.commonservice.model.EmployeeResponseCommonModel;
import com.lehoangtan.commonservice.query.GetDetailsEmployeeQuery;
import com.lehoangtan.employeeservice.command.data.Employee;
import com.lehoangtan.employeeservice.command.data.EmployeeRepository;
import com.lehoangtan.employeeservice.query.model.EmployeeResponseModel;
import com.lehoangtan.employeeservice.query.queries.GetAllEmployeesQuery;
import com.lehoangtan.employeeservice.query.queries.GetEmployeeQuery;

@Component
public class EmployeeProjection {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@QueryHandler
    public EmployeeResponseModel handle(GetEmployeeQuery getEmployeesQuery) {
		EmployeeResponseModel model = new EmployeeResponseModel();
	 Employee employee = employeeRepository.getById(getEmployeesQuery.getEmployeeId());
      BeanUtils.copyProperties(employee, model);

        return model;
    }
	@QueryHandler
	public List<EmployeeResponseModel> handle(GetAllEmployeesQuery getAllEmployeeQuery){
		List<EmployeeResponseModel> listModel = new ArrayList<>();
		List<Employee> listEntity = employeeRepository.findAll();
		listEntity.stream().forEach(s -> {
			EmployeeResponseModel model = new EmployeeResponseModel();
			BeanUtils.copyProperties(s, model);
			listModel.add(model);
		});
		return listModel;
	}
	
	@QueryHandler
	public EmployeeResponseCommonModel handle(GetDetailsEmployeeQuery getDetailsEmployeeQuery) {
		EmployeeResponseCommonModel model = new EmployeeResponseCommonModel();
	 Employee employee = employeeRepository.getById(getDetailsEmployeeQuery.getEmployeeId());
      BeanUtils.copyProperties(employee, model);
        return model;
    }

}
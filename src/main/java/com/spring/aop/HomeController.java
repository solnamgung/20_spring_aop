package com.spring.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private Boss boss;
	
	@Autowired
	private Manager manager;
	
	@Autowired
	private Employee employee;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		
		boss.work();
		manager.work();
		employee.work();
		
		System.out.println("\n\n");
		
		boss.getWorkingTime();
		manager.getWorkingTime();
		employee.getWorkingTime();
		
		System.out.println("\n\n");
		
		boss.getInfo("사장", 1200);
		manager.getInfo("관리자", 700);
		employee.getInfo("직원", 300);
		
		return "home";
		
	}
	
}

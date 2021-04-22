package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;



@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private JavaMailSender mailsender;
	
	//display list of users
	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listemployees",employeeService.getAllEmployees());
		return "index";
	}
	
	@GetMapping("/shownewEmployeeForm")
	public String shownewEmployeeForm(Model model) {
		//model to collect the form data 
		Employee employee =new Employee();
		model.addAttribute("employee",employee);
		return "add_employee";
		
	}
	
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		
		//send the email with the content given
		String email=employee.getEmail();
		SimpleMailMessage message= new SimpleMailMessage();
		message.setFrom("rgp.vicky@gmail.com");
		message.setTo(email);
		String mailSubject = "Springboot Mail";
		String mailcontent ="Hi  "+employee.getName()+ "\n\n"; 
		mailcontent += "This email is for testing my application."+"Yourlogin credentials are follows :" +"\n";
		mailcontent +="Username : "+email+"\n";
		mailcontent +="Password : " +employee.getName()+"_"+employee.getContact()+"\n";
		mailcontent +="Thanks"+"\n"+"Praveen";
		message.setSubject(mailSubject);
		message.setText(mailcontent);
		//save the employee details from the form to the database
		
		employeeService.saveEmployee(employee);
		mailsender.send(message);
		return "redirect:/";
		
	}
	
	
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value="id") long id,Model model) {
		//to get the details off the employee from the service
		Employee employee =employeeService.getEmployeebyId(id);
		//to set the model attribute to pre-populate the form
		model.addAttribute("employee", employee);
		return "update_employee";
		
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value="id") long id) {
		
		//call the delete employee method 
		this.employeeService.deleteEmployee(id);
		return "redirect:/";
	}
	
}

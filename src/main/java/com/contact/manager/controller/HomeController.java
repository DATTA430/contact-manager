package com.contact.manager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.manager.dao.UserRepository;
import com.contact.manager.entities.User;
import com.contact.manager.util.Message;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	// this is handler for registering user
	@PostMapping("/do_register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {
			
			if (result.hasErrors()) {
				model.addAttribute("user",user);
				return "signup";
			}
			if (!agreement) {
				System.out.println("Please Accept the terms and conditions!...");
				throw new Exception("Please Accept the terms and conditions!...");
			} 
			
			else {
				
				user.setRole("ROLE_USER");
				user.setEnabled(true);

				System.out.println("Agreement" + agreement);
				System.out.println("USER" + user);
				userRepository.save(user);
				model.addAttribute("user", new User());
				session.setAttribute("message", new Message("Successfully Registered!!", "alert-success"));
				return "signup";
			}

		} catch (Exception e) {
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}
	

}

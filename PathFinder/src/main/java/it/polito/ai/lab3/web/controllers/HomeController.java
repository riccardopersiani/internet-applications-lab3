package it.polito.ai.lab3.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping({"/", "/home", "/index"})
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public String showHomePage(ModelMap model) {
		return "home";
	}
}

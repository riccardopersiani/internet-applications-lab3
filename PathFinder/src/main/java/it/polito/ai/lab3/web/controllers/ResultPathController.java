package it.polito.ai.lab3.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/resultPath")
public class ResultPathController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String showResult() {
		return "resultPath";
	}
}

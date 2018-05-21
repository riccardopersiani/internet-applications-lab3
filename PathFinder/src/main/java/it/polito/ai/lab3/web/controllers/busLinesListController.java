package it.polito.ai.lab3.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.polito.ai.lab3.repo.entities.BusLine;
import it.polito.ai.lab3.services.buslinesviewer.BusLinesViewerService;

@Controller
@RequestMapping("/busLinesList")
public class busLinesListController {
	
	@Autowired
	private BusLinesViewerService busLinesViewerService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String showBusLinesList(ModelMap model) {
		List<BusLine> busLines = busLinesViewerService.getAllLines();
		
		// Set the bus lines list into the model
		model.addAttribute("busLines", busLines);
		return "busLinesList";
	}
}

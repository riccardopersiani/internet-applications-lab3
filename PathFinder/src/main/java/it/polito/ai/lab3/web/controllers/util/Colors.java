package it.polito.ai.lab3.web.controllers.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Colors {
	private static Colors colors;
	private List<String> colorsArray = new ArrayList<String>();
	private static int index;
	private int size;
	
	private Colors() {
		// Fill the array with colors
		colorsArray.add(0 ,colorToHexString(Color.ORANGE));
		colorsArray.add(1 ,colorToHexString(Color.BLUE));
		colorsArray.add(2 ,colorToHexString(Color.GREEN));
		colorsArray.add(3 ,colorToHexString(Color.RED));
		colorsArray.add(4 ,colorToHexString(Color.YELLOW));
		colorsArray.add(5 ,colorToHexString(Color.CYAN));
		colorsArray.add(6 ,colorToHexString(Color.GRAY));
		colorsArray.add(7 ,colorToHexString(Color.MAGENTA));
				
		index = 0;
		size = colorsArray.size();
	}
	
	public static Colors newInstance() {
		index = 0;
		
		if (colors == null)
			colors = new Colors();
		
		return colors;
	}
	
	public String getNextColor() {
		int i = (index++ % size);
		
		return colorsArray.get(i);
	}
	
	private String colorToHexString(Color c) {
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}
}

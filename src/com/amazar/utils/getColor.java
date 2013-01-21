package com.amazar.utils;

import org.bukkit.Color;

public class getColor {
	public static Color getColorFromString(String col1){
		Color color1 = Color.RED;
		if(col1.equalsIgnoreCase("red")){
			color1 = Color.RED;
		}
		else if(col1.equalsIgnoreCase("aqua")){
			color1 = Color.AQUA;
		}
		else if(col1.equalsIgnoreCase("black")){
			color1 = Color.BLACK;
		}
		else if(col1.equalsIgnoreCase("blue")){
			color1 = Color.BLUE;
		}
		else if(col1.equalsIgnoreCase("fuchsia")){
			color1 = Color.FUCHSIA;
		}
		else if(col1.equalsIgnoreCase("grey")){
			color1 = Color.GRAY;
		}
		else if(col1.equalsIgnoreCase("green")){
			color1 = Color.GREEN;
		}
		else if(col1.equalsIgnoreCase("lime")){
			color1 = Color.LIME;
		}
		else if(col1.equalsIgnoreCase("maroon")){
			color1 = Color.MAROON;
		}
		else if(col1.equalsIgnoreCase("navy")){
			color1 = Color.NAVY;
		}
		else if(col1.equalsIgnoreCase("olive")){
			color1 = Color.OLIVE;
		}
		else if(col1.equalsIgnoreCase("orange")){
			color1 = Color.ORANGE;
		}
		else if(col1.equalsIgnoreCase("pink")){
			color1 = Color.FUCHSIA;
		}
		else if(col1.equalsIgnoreCase("purple")){
			color1 = Color.PURPLE;
		}
		else if(col1.equalsIgnoreCase("silver")){
			color1 = Color.SILVER;
		}
		else if(col1.equalsIgnoreCase("teal")){
			color1 = Color.TEAL;
		}
		else if(col1.equalsIgnoreCase("white")){
			color1 = Color.WHITE;
		}
		else if(col1.equalsIgnoreCase("yellow")){
			color1 = Color.YELLOW;
		}
		else{
			return null;
		}
		return color1;
	}
}

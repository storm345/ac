package com.amazar.utils;


public class UniqueString {
public static String generate(){
	String string = "";
	long time = System.currentTimeMillis();
	time = Math.round(time*10)/10;
	int rand = 1 + (int)(Math.random() * ((9 - 1) + 1));
	if(rand > 5){
		string = "F";
				
	}
	else if(rand > 5){
		string = "E";
	}
	else{
		string = "D";
	}
	int rand2 = 1 + (int)(Math.random() * ((9 - 1) + 1));
	string = string + rand + time + rand2;
	return string;
}
}

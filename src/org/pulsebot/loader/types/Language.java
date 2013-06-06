package org.pulsebot.loader.types;

public enum Language {

	ENGLISH(0),
	GERMAN(1),
	FRENCH(2),
	PORTUGUESE(3),
	SPANISH(6);
	
	private final int number;
	
	private Language(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return number;
	}
	
}

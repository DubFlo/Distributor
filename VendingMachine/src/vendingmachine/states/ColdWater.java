package vendingmachine.states;

import vendingmachine.components.*;

public class ColdWater extends Problem {

	private static ColdWater instance;

	//Singleton design pattern
	private ColdWater() {}
	public static ColdWater Instance() {
		if (instance == null) instance = new ColdWater();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "Water is too cold. Please wait a moment...";
	}

}
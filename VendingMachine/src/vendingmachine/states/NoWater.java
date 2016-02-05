package vendingmachine.states;

import vendingmachine.components.*;

public class NoWater extends Problem {

	private static NoWater instance;

	//Singleton design pattern
	private NoWater() {}
	public static NoWater Instance() {
		if (instance == null) instance = new NoWater();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "Water supply is off. No drink can be ordered";
	}

}
package vendingmachine.states;

import vendingmachine.components.*;

public class NoCup extends Problem {

	private static NoCup instance;

	//Singleton design pattern
	private NoCup() {}
	public static NoCup Instance() {
		if (instance == null) instance = new NoCup();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "No cup is available. No drink can be ordered";
	}

}
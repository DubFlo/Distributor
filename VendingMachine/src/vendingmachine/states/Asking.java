package vendingmachine.states;

import vendingmachine.components.*;

public class Asking extends State {

	private static Asking instance;

	//Singleton design pattern
	private Asking() {}
	public static Asking Instance() {
		if (instance == null) instance = new Asking();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "Choose your sugar quantity on the small screen";
	}

}
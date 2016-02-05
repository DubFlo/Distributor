package vendingmachine.states;

import vendingmachine.components.*;

public class Preparing extends State {

	private static Preparing instance;

	//Singleton design pattern
	private Preparing() {}
	public static Preparing Instance() {
		if (instance == null) instance = new Preparing();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "Your drink is in preparation...";
	}
	
	public void coinInserted(Coin coin, Context c){
		observer.setTemporaryNorthText("Not possible to insert coin ");
	}
}
// retirer du stock la boisson à la fin du Timer
// Changer d'état + lancer la préparation de la boisson
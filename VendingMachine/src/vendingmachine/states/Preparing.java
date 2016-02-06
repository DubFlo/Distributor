package vendingmachine.states;

import javax.swing.Timer;

import vendingmachine.components.*;

public class Preparing extends State {

	private static Preparing instance;

	//Singleton design pattern
	private Preparing() {
		//Timer t = new Timer(5000, );
	}
	
	public static Preparing Instance() {
		if (instance == null) instance = new Preparing();
		return instance;
	}
	
	@Override
	public String getDefaultText(Context c) {
		return "Your drink is in preparation...";
	}
	
	public void coinInserted(Coin coin, Context c){
		c.getObserver().setTemporaryNorthText("Not possible to insert coin"); 
		//Si la fente est l�, on devrait pouvoir ins�rer la pi�ce mais elle ressort directement
	}
}
// retirer du stock la boisson � la fin du Timer
// Changer d'�tat + lancer la pr�paration de la boisson
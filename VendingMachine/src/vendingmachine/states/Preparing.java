package vendingmachine.states;

import javax.swing.Timer;

import vendingmachine.components.*;

public class Preparing extends State {

	private static Preparing instance;
	private static Timer timer; //Le mettre static ?

	//Singleton design pattern
	private Preparing(Context c) { //Pas propre de passer le context en param�tre 
								   //mais comment sinon changer son �tat apr�s 5 secondes?
		int delay = 2000;
		timer = new Timer(delay, e -> c.changeState(Idle.Instance()));
		timer.setRepeats(false);
	}
	
	public static Preparing Instance(Context c) {
		if (instance == null) {
			instance = new Preparing(c);
		}
		timer.restart();
		return instance;
	}
	
	@Override
	public void exit(Context c) {
		 c.getObserver().setCupBool(true);
	}
	
	@Override
	public String getDefaultText(Context c) {
		return "Your drink is in preparation...";
	}
	
	public void coinInserted(Coin coin, Context c){
		super.coinInserted(coin, c);
		c.getObserver().setTemporaryNorthText("Please wait for the end of the preparation..."); 
		//Si la fente est l�, on devrait pouvoir ins�rer la pi�ce mais elle ressort directement
	}
}
// retirer du stock la boisson � la fin du Timer
// Changer d'�tat + lancer la pr�paration de la boisson
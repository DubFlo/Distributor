package vendingmachine.states;

import vendingmachine.components.*;

public class Asking extends State {

	private static Asking instance;
	private byte chosenSugar; //Dans Context aussi ?
	
	//Singleton design pattern
	private Asking() {
		chosenSugar = 0;
	}
	
	public static Asking Instance() {
		if (instance == null) instance = new Asking();
		return instance;
	}
	
	@Override
	public String getDefaultText(Context c) {
		return "Choose your sugar quantity with + and -";
	}
	
	@Override
	public void more(Context c) {
		if (chosenSugar < 5 && c.getStock().isSugarInStock(chosenSugar + 1)) {
			chosenSugar += 1;
			c.getObserver().setSugarText(getSugarText()); //Faire plus simple ??
		}
		else if (chosenSugar == 5){
			c.getObserver().setTemporaryNorthText("Maximum quantity of sugar : 5");
		}
		else {
			c.getObserver().setTemporaryNorthText("No more sugar in stock");
		}
	}
	
	@Override
	public void less (Context c) {
		if (chosenSugar > 0 && c.getStock().isSugarInStock(chosenSugar + 1)) {
			chosenSugar -= 1;
			c.getObserver().setSugarText(getSugarText());
		}
	}
	
	@Override
	public void confirm(Context c){
		c.getStock().removeSugarCubes(chosenSugar);
		c.getChangeMachine().giveChange();  //On a vérifié que le change était possible dans Idle()
		c.setAmountInside(0);
		chosenSugar = 0;
		c.changeState(Preparing.Instance(c));
	}

	@Override
	public String getSugarText() {
		return "Sugar: " + chosenSugar + "/5";  //Affichage plus joli ?
	}
	
	@Override
	public void cancel(Context c) {
		super.cancel(c);
		chosenSugar = 0;
		c.changeState(Idle.Instance());
	}
}
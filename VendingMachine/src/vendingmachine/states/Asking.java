package vendingmachine.states;

import vendingmachine.components.*;

public class Asking extends State {

	private static Asking instance;
	private byte chosenSugar = 0;
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
	@Override
	public void more(Context c){
		if (chosenSugar < 5 && c.getStock().isSugarInStock(chosenSugar + 1) )
		{
			chosenSugar += 1;
			c.getObserver().setSugarText("Sugar :" + chosenSugar + "/5");
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
		if (chosenSugar > 0 && c.getStock().isSugarInStock(chosenSugar + 1) )
		{
			chosenSugar -= 1;
			c.getObserver().setSugarText("Sugar :" + chosenSugar + "/5");
		}
	}
	
	@Override
	public void confirm(Context c){
		c.getStock().setSugarCubesNbr(0);
		c.changeState(Preparing.Instance());
	}

}
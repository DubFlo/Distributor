package vendingmachine.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.components.*;

public abstract class State {

	protected Drink drinkChosen;
	
	protected static final Logger log = LogManager.getLogger("State");

	public void entry(Context c) {
		c.getObserver().setNorthText(getDefaultText(c));
		c.getObserver().setSugarText(getSugarText());
		c.getObserver().setInfo();
	}

	public void exit(Context c) {}
	
	public abstract String getDefaultText(Context c);
	public void coinInserted(Coin coin, Context c) {
		c.getObserver().setChangeBool(true);
	} //toujours fait le bruit de pièces + afficher le montant rendu; à faire
	
	//These buttons do nothing by default
	public void drinkButton(Drink drink, Context c) {}
	public void less(Context c) {}
	public void more(Context c) {}
	public void confirm(Context c) {}
	
	public void cancel(Context c) {
		if (c.getAmountInside() > 0 && c.getChangeMachine().isChangePossible(c.getAmountInside())) {
										//Cette condition est toujours vraie mais fait les calculs; bof
			c.getChangeMachine().giveChange();
			if (c.getAmountInside() > 0) {
				c.getObserver().setChangeBool(true);
			}
			c.setAmountInside(0);
			//c.getObserver().setInfo(); //Pas très propre mais il faut actualiser l'UI
		}	
	}
	
	public String getSugarText() {
		return "";
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName(); //instead of getName() to avoid the package name
	}

}
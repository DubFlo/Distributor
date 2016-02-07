package vendingmachine.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.components.*;

public abstract class State {

	protected Drink drinkChosen;
	
	protected static final Logger log = LogManager.getLogger("State");

	public void entry(Context c) {
		c.setNorthText(getDefaultText(c));
		c.setSugarText(getSugarText(c));
		c.setInfo();
		log.info("State " + this.getClass().getSimpleName() + " entered.");
	}

	public void exit(Context c) {}
	
	public abstract String getDefaultText(Context c);
	public void coinInserted(Coin coin, Context c) {
		c.setChangeBool(true);
		log.info(coin.VALUE/100.0 + " € inserted but not allowed.");
	} //afficher le montant rendu; à faire
	
	//These buttons do nothing by default
	public void drinkButton(Drink drink, Context c) {}
	public void less(Context c) {}
	public void more(Context c) {}
	public void confirm(Context c) {}
	
	public void cancel(Context c) {
		if (c.getAmountInside() > 0 && c.getChangeMachine().isChangePossible(c.getAmountInside())) {
										//Cette condition est toujours vraie mais fait les calculs; bof
			c.giveChange();
			c.getState().entry(c); //ou this.entry(c) non ???
		}	
	}
	
	public String getSugarText(Context c) {
		return "";
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName(); //instead of getName() to avoid the package name
	}

}
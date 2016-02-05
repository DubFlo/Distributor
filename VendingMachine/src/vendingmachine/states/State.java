package vendingmachine.states;

import vendingmachine.components.*;
import vendingmachine.ui.ContextListener;

public abstract class State {

	protected ContextListener observer;
	protected byte chosenSugar = 0;
	protected Drink drinkChosen;
	protected int amountInside;

	public void entry() {
		observer.setNorthText(getDefaultText());
		observer.setSugarText(getSugarText());
		observer.setInfo();
	}

	public void exit() {
		
	}
	
	public abstract String getDefaultText();
	public void coinInserted(Coin coin, Context c) {
		observer.setChangeBool(true);
	} //toujours fait le bruit de pièces + afficher le montant rendu; à faire
	
	//These buttons do nothing by default
	public void drinkButton(Drink drink, Context c) {}
	public void less(Context c) {}
	public void more(Context c) {}
	public void cancel(Context c) {
		if (amountInside > 0) {
			c.getChangeMachine().giveChange(amountInside);
			chosenSugar = 0;
			c.changeState(Idle.Instance());
		}
	}
	public void confirm(Context c) {}
	public String getSugarText() {return "";}
	
	public void setContextListener(ContextListener o) {
		observer = o;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName(); //instead of getName() to avoid the package
	}

	public int getAmountInside() {
		return amountInside;
	}

	public void setAmountInside(int amountInside) {
		this.amountInside = amountInside;
	}
}
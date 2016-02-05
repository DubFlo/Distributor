package vendingmachine.states;

import vendingmachine.components.*;
import vendingmachine.ui.ContextListener;

public abstract class State {

	protected ContextListener observer;
	protected byte sugar = 0;
	protected int amountInside;

	public void entry() {
		observer.setNorthText(getDefaultText());
		observer.setSugarText(getSugarText());
		observer.setInfo();
	}

	public void exit() {
		
	}
	
	public abstract String getDefaultText();
	public void coinInserted(Coin coin, Context c) {} //toujours fait le bruit de pièces; à faire
	
	//These buttons do nothing by default
	public void drinkButton(Drink drink, Context c) {}
	public void less() {}
	public void more() {}
	public void cancel(Context c) {}
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
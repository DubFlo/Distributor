package vendingmachine.states;

import vendingmachine.components.*;
import vendingmachine.ui.ContextListener;

public abstract class State {

	protected ContextListener observer;
	protected boolean changeBool;
	protected boolean cupBool;

	public void entry() {
		observer.setNorthText(getDefaultText());
		//observer.setSugarText(getSugarText());
		observer.setInfo();
	}

	public abstract String getDefaultText();
	
	public void drinkButton(Drink drink, Context c) {}
	public void less() {} //By default does nothing
	public void more() {} //By default does nothing
	public void cancel(Context c) {}
	public void confirm(Context c) {}

	public abstract void coinInserted(Coin coin, Context c);
	
	public void setContextListener(ContextListener o) {
		observer = o;
	}
	
	public void takeChange() {
		// TODO - implement State.takeChange
		
	}

	public void takeCup() {
		// TODO - implement State.takeCup
		
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName(); //instead of getName() to avoid the package
	}

}
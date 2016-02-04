package vendingmachine.states;

import vendingmachine.components.*;

public abstract class State {

	protected boolean changeBool;
	protected boolean cupBool;

	public abstract void entry();

	public static abstract State getInstance();

	public abstract void drinkButton(Drink drink, Context c);

	public abstract void less();

	public abstract void more();

	public abstract void cancel(Context c);

	public abstract void confirm(Context c);

	public abstract void coinInserted(Coin coin, Context c);

	public void takeChange() {
		// TODO - implement State.takeChange
		
	}

	public void takeCup() {
		// TODO - implement State.takeCup
		
	}

}
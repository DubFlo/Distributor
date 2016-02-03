package vendingmachine.components;

import vendingmachine.ui.*;
import vendingmachine.states.*;

public class Context implements EventListener {

	private String northText;
	private String sugarText;
	private int amountInside;
	private ContextListener observer;

	/**
	 * 
	 * @param drinks
	 * @param cm
	 * @param stock
	 */
	public Context(Drink[] drinks, ChangeMachine cm, Stock stock) {
		// TODO - implement Context.Context
		
	}

	/**
	 * 
	 * @param state
	 */
	public void changeState(State state) {
		// TODO - implement Context.changeState
		
	}

	public void playAlarmSound() {
		// TODO - implement Context.playAlarmSound
		
	}

	@Override
	public void drinkButton(Drink drink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void less() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void more() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinInserted(Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeCup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Drink[] getDrinks() {
		// TODO Auto-generated method stub
		return null;
	}

}
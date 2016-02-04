package vendingmachine.components;

import java.util.List;

import vendingmachine.ui.*;
import vendingmachine.states.*;

public class Context implements EventListener {

	private ChangeMachine changeMachine;
	private Stock stock;
	private List drinkList;
	private HeatingSystem heatingSystem;
	
	private State state;
	
	private String northText;
	private String sugarText;
	private int amountInside;
	private ContextListener observer;

	public Context(List drinkList, ChangeMachine changeMachine, Stock stock) {
		this.drinkList = drinkList;
		this.changeMachine = changeMachine;
		this.stock = stock;
		
		//Thread ??
		heatingSystem = new HeatingSystem();
	}

	public void changeState(State state) {
		this.state = state.getInstance();
		this.state.entry();
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
	public List<Drink> getDrinks() {
		return drinkList;
	}

	@Override
	public void setContextListener(ContextListener o) {
		observer = o;
		
	}

	public State getState() {
		return state;
	}

}
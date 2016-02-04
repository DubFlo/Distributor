package vendingmachine.components;

import java.util.List;
import java.util.Map;

import vendingmachine.ui.*;
import vendingmachine.states.*;

public class Context implements EventListener {

	private ChangeMachine changeMachine;

	private Stock stock;
	private List<Drink> drinkList;
	private HeatingSystem heatingSystem;
	
	private State state;
	
	private int amountInside;
	private ContextListener observer;

	public Context(List<Drink> drinkList, ChangeMachine changeMachine, Stock stock) {
		this.drinkList = drinkList;
		this.changeMachine = changeMachine;
		this.stock = stock;
		this.state = Idle.Instance();
		
		//Thread ??
		heatingSystem = new HeatingSystem();
	}

	public void changeState(State state) {
		this.state = state;
		this.state.entry();
	}

	public void playAlarmSound() {
		// TODO - implement Context.playAlarmSound
		
	}

	@Override
	public void drinkButton(Drink drink) {
		state.drinkButton(drink, this);
	}

	@Override
	public void less() {
		state.less();
	}

	@Override
	public void more() {
		state.more();
	}

	@Override
	public void cancel() {
		state.cancel(this);
	}

	@Override
	public void confirm() {
		state.confirm(this);
	}

	@Override
	public void coinInserted(Coin coin) {
		state.coinInserted(coin, this);
	}

	@Override
	public void takeChange() {
		state.takeChange();
	}

	@Override
	public void takeCup() {
		state.takeCup();
	}

	@Override
	public List<Drink> getDrinks() {
		return drinkList;
	}

	@Override
	public void setContextListener(ContextListener o) {
		state.setContextListener(o);	
	}
	
	public ContextListener getObserver() {
		return observer;
	}
	
	public State getState() {
		return state;
	}

	@Override
	public String getInfo() {
		String info = "State: " + getState() + "\n";
		info += "\nDrinks: \n";
		Map<Drink, Integer> s = stock.getDrinkQty();
		for (int i = 0; i < 8; i++) {
			info += drinkList.get(i).getName() + ": " + s.get(drinkList.get(i)) + " available.\n";
		}
		
		info += "\nCoins:\n";
		for (int i = 0; i < 8; i++) {
			info += ChangeMachine.COINS_TEXT[i] + ": "
					+ changeMachine.getCoinsStock().get(ChangeMachine.COINS[i]) + " available.\n";
		}
		
		info += "\nCurrent temperature: " + getTemperature() + "°C\n";
		info += "\n" + stock.getCupsNbr() + " cup(s) available.\n"
				+ stock.getSugarCubesNbr() + " sugar cube(s) available.\n"
				+ stock.getSpoonsNbr() + " spoon(s) available.\n";
		
		return info;
	}

	private double getTemperature() {
		return heatingSystem.getTemperature();
	}
	
	public ChangeMachine getChangeMachine() {
		return changeMachine;
	}

	public int getAmountInside() {
		return amountInside;
	}

	public void setAmountInside(int amountInside) {
		this.amountInside = amountInside;
	}

	public Stock getStock() {
		return stock;
	}
	
}
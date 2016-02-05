package vendingmachine.states;

import vendingmachine.components.*;

public class Idle extends State {

	private static Idle instance;
	
	//Singleton design pattern
	private Idle() {}
	public static Idle Instance() {
		if (instance == null) instance = new Idle();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		return "Please make your choice";
	}
	
	@Override
	public void drinkButton(Drink drink, Context c) {
		if (!c.getStock().isCupInStock()) {
			observer.setTempNorthText("Cups are out of stock. No drink can be ordered");
		}
		if (!c.getStock().isDrinkInStock(drink)) {
			observer.setTempNorthText("Drink out of stock (otherwise " + drink.getPrice()/100.0 + " €)");
		}
		else if (drink.getPrice() > c.getAmountInside()) {
			observer.setTempNorthText("Price: " + drink.getPrice()/100.0 + " €");
		}
		else if (c.getChangeMachine().giveChange(c.getAmountInside() - drink.getPrice()) == 1) {
			if (drink.isSugar())
				c.changeState(Asking.Instance());
			else {
				c.changeState(Preparing.Instance());
			}
		}
		else {
			observer.setTempNorthText("Unable to give the exact change");
		}
	}
	
	@Override
	public void coinInserted(Coin coin, Context c) {
		super.coinInserted(coin, c);
		if (c.getChangeMachine().isCoinAccepted(coin)) {
			c.setAmountInside(c.getAmountInside() + coin.VALUE);
			observer.setTempNorthText(Double.toString(coin.VALUE/100.0) + " € inserted");
		}
		else {
			observer.setChangeBool(true);
			observer.setTempNorthText("Coin not recognized by the machine");
		}
	}
}
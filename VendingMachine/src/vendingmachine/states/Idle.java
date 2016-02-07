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
	public String getDefaultText(Context c) {
		String msg = "Please make your choice";
		if (c.getAmountInside() > 0) {
			msg += (" (" + c.getAmountInside()/100.0 + " € entered)");
		}
		return msg;
	}
	
	@Override
	public void drinkButton(Drink d, Context c) {
		if (!c.getStock().isCupInStock()) { //Géré dans NoCup ??
			c.getObserver().setTemporaryNorthText("Cups are out of stock. No drink can be ordered");
		}
		else if (!c.getStock().isDrinkInStock(d)) {
			c.getObserver().setTemporaryNorthText("Drink out of stock (otherwise " + d.getPrice()/100.0 + " €)");
		}
		else if (c.isCupInside()) {
			c.getObserver().setTemporaryNorthText("Please remove the drink before ordering");
		}
		else if (d.getPrice() > c.getAmountInside()) {
			c.getObserver().setTemporaryNorthText("Price: " + d.getPrice()/100.0 + " €");
		}
		else if (c.getChangeMachine().isChangePossible(c.getAmountInside() - d.getPrice())) {
			c.setChosenDrink(d);
			if (d.isSugar())
				c.changeState(Asking.Instance());
			else {
				c.getChangeMachine().giveChange();
				c.setAmountInside(0);
				c.changeState(Preparing.Instance(c));
			}
		}
		else {
			c.getObserver().setTemporaryNorthText("Unable to give the exact change");
		}
	}
	
	@Override
	public void coinInserted(Coin coin, Context c) {
		if (c.getChangeMachine().isCoinAccepted(coin)) {
			c.setAmountInside(c.getAmountInside() + coin.VALUE);
			c.getChangeMachine().insertCoin(coin);
			c.getObserver().setTemporaryNorthText(Double.toString(coin.VALUE/100.0) + " € inserted");
			log.info(coin.VALUE/100.0 + " € inserted.");
		}
		else {
			super.coinInserted(coin, c);
			c.getObserver().setTemporaryNorthText("Coin not recognized by the machine");
			log.info(coin.VALUE/100.0 + " € inserted but not allowed.");
		}
		c.getObserver().setInfo(); //Le mettre ici ?????
	}
}
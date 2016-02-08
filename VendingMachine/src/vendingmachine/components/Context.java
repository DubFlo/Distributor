package vendingmachine.components;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.SoundLoader;
import vendingmachine.ui.*;
import vendingmachine.states.*;

public class Context implements EventListener {

	private ChangeMachine changeMachine;
	private Stock stock;
	private List<Drink> drinkList; //Plutôt Collection pour pas d'ordre ????
	private HeatingSystem heatingSystem;
	
	private State state;
	private int amountInside;
	private boolean cupInside;
	private Drink chosenDrink;
	private byte chosenSugar;
	
	private ContextListener observer;
	
	private static final Logger log = LogManager.getLogger("Context");
	
	public Context(List<Drink> drinkList, ChangeMachine changeMachine, Stock stock) {
		this.drinkList = drinkList;
		this.changeMachine = changeMachine;
		this.stock = stock;
		this.heatingSystem = new HeatingSystem();
		
		this.amountInside = 0;
		this.chosenSugar = 0;
		this.cupInside = false;
		
		log.info("New Vending Machine Created");
	}

	@Override
	public void changeState(State newState) {
		if (state != null) {
			state.exit(this);
		}
		state = newState;
		state.entry(this);
	}

	public void playAlarmSound() {
		SoundLoader.play(SoundLoader.beep);
	}

	@Override
	public void drinkButton(Drink drink) {
		state.drinkButton(drink, this);
	}

	@Override
	public void less() {
		state.less(this);
	}

	@Override
	public void more() {
		state.more(this);
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
	public void takeCup() {
		setCupBool(false);
		SoundLoader.beep.stop(); //stop the sound effect is the cup is taken.
		log.info("Cup taken.");
	}

	@Override
	public void takeChange() {
		setChangeBool(false);
		SoundLoader.cling.stop();
		log.info("Change taken.");
	}
	
	@Override
	public List<Drink> getDrinks() {
		return drinkList;
	}

	@Override
	public <T extends ContextListener & TemperatureListener> void setObserver(T o) {
		this.observer = o;
		heatingSystem.setObserver(o);
	}
	
	public ContextListener getObserver() {
		return observer;
	}

	@Override
	public State getState() {
		return state;
	}
	
	@Override
	public String getNorthText() {
		return state.getDefaultText(this);
	}

	@Override
	public String getInfo() {
		String info = "State: " + getState() + "\n";
		info += "\n" + amountInside/100.0 + " € inserted.\n";
		info += "\nDrinks: \n";
		Map<Drink, Integer> s = stock.getDrinkQty();
		for (int i = 0; i < VendingMachineGUI.NBR_DRINKS; i++) {
			info += drinkList.get(i).getName() + ": " + s.get(drinkList.get(i)) + " available.\n";
		}
		
		info += "\nCoins:\n";
		Map<Coin, Integer> cs = changeMachine.getCoinsStock();
		for (int i = 0; i < ChangeMachine.COINS.length; i++) {
			info += ChangeMachine.COINS_TEXT[i] + ": "
					+ cs.get(ChangeMachine.COINS[i]) + " available.\n";
		}
		
		info += "\n" + stock.getCupsNbr() + " cup(s) available.\n"
				+ stock.getSugarCubesNbr() + " sugar cube(s) available.\n"
				+ stock.getSpoonsNbr() + " spoon(s) available.\n";
		
		return info;
	}

	public double getTemperature() {
		return heatingSystem.getTemperature();
	}
	
	public ChangeMachine getChangeMachine() {
		return changeMachine;
	}

	public Stock getStock() {
		return stock;
	}
	
	public boolean isCupInside() {
		return cupInside;
	}
	
	public HeatingSystem getHeatingSystem() {
		return heatingSystem;
	}

	public int getAmountInside() {
		return amountInside;
	}

	public void setAmountInside(int amountInside) {
		this.amountInside = amountInside;
	}
	
	@Override
	public void setCupBool(boolean b) {
		observer.setCupBool(b);
		cupInside = b;
	}

	@Override
	public void setChangeBool(boolean b) {
		observer.setChangeBool(b);
		if (b) SoundLoader.play(SoundLoader.cling);
	}

	public Drink getChosenDrink() {
		return chosenDrink;
	}

	public void setChosenDrink(Drink chosenDrink) {
		this.chosenDrink = chosenDrink;
	}

	public void setTemporaryNorthText(String msg) {
		observer.setTemporaryNorthText(msg);
	}

	public void setInfo() {
		observer.setInfo(getInfo());
	}

	public void setSugarText(String msg) {
		observer.setSugarText(msg);
	}

	public byte getChosenSugar() {
		return chosenSugar;
	}

	public void decrementChosenSugar() {
		chosenSugar -= 1;
	}

	public void resetChosenSugar() {
		chosenSugar = 0;
	}
	
	public void incrementChosenSugar() {
		chosenSugar += 1;
	}

	public void setNorthText(String msg) {
		observer.setNorthText(msg);
	}
	
	public void giveChange() {
		changeMachine.giveChange();
		if (amountInside > 0) {
			setChangeBool(true);
		}
		SoundLoader.play(SoundLoader.cling);
		amountInside = 0;
	}

	public void insertCoin(Coin coin) {
		changeMachine.insertCoin(coin);
		SoundLoader.play(SoundLoader.fop);
	}
	
}
package vendingmachine.components;

import java.util.List;

import vendingmachine.states.State;
import vendingmachine.ui.ContextListener;
import vendingmachine.ui.TemperatureListener;

public interface EventListener {

	void drinkButton(Drink drink);
	void less();
	void more();
	void cancel();
	void confirm();
	void coinInserted(Coin coin);
	
	void changeState(State state);
	State getState();

	List<Drink> getDrinks();
	String getInfo();
	<T extends ContextListener & TemperatureListener> void setObserver(T o);
}
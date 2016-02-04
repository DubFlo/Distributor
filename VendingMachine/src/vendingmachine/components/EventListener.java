package vendingmachine.components;

import java.util.List;

import vendingmachine.states.State;
import vendingmachine.ui.ContextListener;

public interface EventListener {

	void drinkButton(Drink drink);
	void less();
	void more();
	void cancel();
	void confirm();
	void coinInserted(Coin coin);
	void takeChange();
	void takeCup();
	
	void changeState(State state);
	State getState();

	List<Drink> getDrinks();
	String getInfo();
	void setContextListener(ContextListener c);
}
package vendingmachine.components;

import java.util.List;
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

	List<Drink> getDrinks();

	void setContextListener(ContextListener c);
}
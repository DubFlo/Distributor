package vendingmachine.components;

public interface EventListener {

	void drinkButton(Drink drink);

	void less();

	void more();

	void cancel();

	void confirm();

	void coinInserted(Coin coin);

	void takeChange();

	void takeCup();

	Drink[] getDrinks();

}
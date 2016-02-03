package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

public class Stock {

	private int sugarCubesNbr;
	private int cupsNbr;
	private int spoonsNbr;
	private Map<Drink, Integer> drinkQty;

	public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Hashtable<Drink, Integer> drinkQty) {
		this.sugarCubesNbr = sugarCubesNbr;
		this.cupsNbr = cupsNbr;
		this.spoonsNbr = spoonsNbr;
		this.drinkQty = drinkQty;
	}

	/**
	 * 
	 * @param sugar
	 */
	public boolean isSugarInStock(int sugar) {
		return sugar <= sugarCubesNbr;
	}

	public boolean isCupInStock() {
		return cupsNbr > 0;
	}

	public boolean isSpoonInStock() {
		return spoonsNbr > 0;
	}

	public boolean isDrinkInStock(Drink drink) {
		return drinkQty.get(drink) > 0;
	}

}
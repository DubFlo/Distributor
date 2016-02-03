package vendingmachine.components;

import java.util.Hashtable;

public class Stock {

	private int sugarCubesNbr;
	private int cupsNbr;
	private int spoonsNbr;
	private Hashtable<Drink, Integer> drinkQty;

	/**
	 * 
	 * @param sugarCubesNbr
	 * @param cupsNbr
	 * @param spoonsNbr
	 * @param drinkQty
	 */
	public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Hashtable<Drink, Integer> drinkQty) {
		// TODO - implement Stock.Stock
		
	}

	/**
	 * 
	 * @param sugar
	 */
	public boolean isSugarInStock(int sugar) {
		return sugar <= sugarCubesNbr;
	}

	public boolean isCupInStock() {
		return cupsNbr != 0;
	}

	public boolean isSpoonInStock() {
		return spoonsNbr != 0;
	}

	public boolean isDrinkInStock(Drink drink) {
		return drinkQty.get(drink) != 0;
	}

}
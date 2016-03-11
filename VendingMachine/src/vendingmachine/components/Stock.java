package vendingmachine.components;

import java.util.Map;

/**
 * The Stock class lists all the stock values needed for a drinks vending machine 
 * (sugar cubes, cups, spoons and drinks).
 */
public class Stock {

  private int sugarCubesNbr;
  private int cupsNbr;
  private int spoonsNbr;
  
  /**
   * A Map that maps to each Drink its stock as an Integer.
   */
  private final Map<Drink, Integer> drinkQty;

  public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Map<Drink, Integer> drinkQty) {
    this.sugarCubesNbr = sugarCubesNbr;
    this.cupsNbr = cupsNbr;
    this.spoonsNbr = spoonsNbr;
    this.drinkQty = drinkQty;
  }

  /**
   * @return the number of cups left in the machine.
   */
  public int getCupsNbr() {
    return cupsNbr;
  }

  /**
   * @return a Map of all the drinks and their stock values
   */
  public Map<Drink, Integer> getDrinkQty() {
    return drinkQty;
  }

  /**
   * @return the number of spoons left in the machine
   */
  public int getSpoonsNbr() {
    return spoonsNbr;
  }

  /**
   * @return the number of sugar cubes left in the machine
   */
  public int getSugarCubesNbr() {
    return sugarCubesNbr;
  }

  /**
   * @return true if there is a cup in stock, false otherwise
   */
  public boolean isCupInStock() {
    return cupsNbr > 0;
  }

  /**
   * @param drink the Drink that may be in stock
   * @return true if there is a {@code drink} in stock, false otherwise
   */
  public boolean isDrinkInStock(Drink drink) {
    return drinkQty.get(drink) > 0;
  }

  /**
   * @return true if there is a spoon in stock, false otherwise
   */
  public boolean isSpoonInStock() {
    return spoonsNbr > 0;
  }

  /**
   * @param sugar the number of sugar cubes that may be in stock
   * @return true if there is at least {@code sugar} sugar cubes in stock.
   */
  public boolean isSugarInStock(int sugar) {
    return sugar <= sugarCubesNbr;
  }

  /**
   * Removes one cup from the stock.
   */
  public void removeCup() {
    cupsNbr -= 1;
  }

  /**
   * Removes one {@code drink} from the stock.
   * @param drink the Drink to remove
   */
  public void removeDrink(Drink drink) {
    drinkQty.put(drink, drinkQty.get(drink) - 1);
  }

  /**
   * Removes one spoon from the stock.
   */
  public void removeSpoon() {
    spoonsNbr -= 1;
  }

  /**
   * @param Removes {@code i} sugar cubes from the stock.
   */
  public void removeSugarCubes(int i) {
    sugarCubesNbr -= i;
  }
  
}
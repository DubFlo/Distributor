package vendingmachine.components;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Stock class lists all the stock values needed for a drinks vending machine 
 * (sugar cubes, cups, spoons and drinks).
 */
public class Stock {

  private static final Logger log = LogManager.getLogger("Stock");
  
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
    if (isCupInStock()) {
      log.info(cupsNbr + " cups remaining.");
    } else {
      log.warn("No more cups!");
    }
  }

  /**
   * Removes one {@code drink} from the stock.
   * @param drink the Drink to remove
   */
  public void removeDrink(Drink drink) {
    drinkQty.put(drink, drinkQty.get(drink) - 1);
    if (isDrinkInStock(drink)) {
      log.info(drink.getName() + " prepared (" + drinkQty.get(drink) + " remaining).");
    } else {
      log.warn(drink.getName() + " prepared, no more in stock!");
    }
  }

  /**
   * Removes one spoon from the stock.
   */
  public void removeSpoon() {
    spoonsNbr -= 1;
    if (isSpoonInStock()) {
      log.info(spoonsNbr + " spoons remaining.");
    } else {
      log.warn("No more spoon in stock!");
    }
  }

  /**
   * @param Removes {@code i} sugar cubes from the stock.
   */
  public void removeSugarCubes(int i) {
    sugarCubesNbr -= i;
    if (sugarCubesNbr > 0) {
      log.info(i + " sugar cubes ordered (" + sugarCubesNbr + " remaining).");
    } else {
      log.warn("No more sugar in stock!");
    }
  }

  /**
   * @return a String containing all the information about the current stock.
   */
  public String getInfo() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Drink(s): \n");
    for (Drink drink: drinkQty.keySet()) {
      sb.append(drink.getName()).append(": ")
      .append(drinkQty.get(drink)).append(" available.\n");
    }
    
    sb.append("\n")
    .append(cupsNbr).append(" cup(s) available.\n")
    .append(sugarCubesNbr).append(" sugar cube(s) available.\n")
    .append(spoonsNbr).append(" spoon(s) available.\n");
    return sb.toString();
  }

  /**
   * Updates the stock of {@code drink} to the {@code value} specified.
   * Logs the operation done.
   * 
   * @param drink the Drink whose stock must be changed
   * @param value the new value for the {@code drink} (must be positive)
   */
  public void setDrinkQty(Drink drink, int value) {
    final int difference = value - drinkQty.get(drink);
    if (difference > 0) {
      log.info(difference + " " + drink.getName() + " resupplied.");
    } else if (difference < 0) {
      log.info(-difference + " " + drink.getName() + " removed from the stock.");
    }
    
    drinkQty.put(drink, value);
  }
  
}
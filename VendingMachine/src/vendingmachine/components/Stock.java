package vendingmachine.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vendingmachine.Drink;
import vendingmachine.Utils;
import vendingmachine.states.NoCup;

/**
 * The Stock class lists all the stock values needed for a drinks vending machine
 * (sugar cubes, cups, spoons and drinks).
 */
public class Stock {

  /*
   * The number of sugar, cups and spoons in stock.
   */
  private int sugarCubesNbr;
  private int cupsNbr;
  private int spoonsNbr;

  /**
   * A Map that maps to each Drink its stock as an Integer.
   */
  private final Map<Drink, Integer> drinkQty;

  /**
   * Creates a Stock with the specified values.
   * Throws an IllegalArgumentException if a value is negative.
   * 
   * @param sugarCubesNbr the number of sugar cubes
   * @param cupsNbr the number of cups
   * @param spoonsNbr the number of spoons
   * @param drinkQty a Map mapping each Drink to its stock value
   */
  public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Map<Drink, Integer> drinkQty) {
    Utils.checkPositiveIntIllegal(sugarCubesNbr, "sugar stock");
    Utils.checkPositiveIntIllegal(cupsNbr, "cups stock");
    Utils.checkPositiveIntIllegal(spoonsNbr, "spoons stock");
    for (Integer i: drinkQty.values()) {
      Utils.checkPositiveIntIllegal(i, "drinks");
    }
    this.sugarCubesNbr = sugarCubesNbr;
    this.cupsNbr = cupsNbr;
    this.spoonsNbr = spoonsNbr;
    this.drinkQty = drinkQty;
  }

  /**
   * @param sugar the number of sugar cubes that may be in stock
   * @return true if there is at least {@code sugar} sugar cubes in stock.
   */
  public boolean isSugarInStock(int sugar) {
    return sugar <= sugarCubesNbr;
  }

  /**
   * @return true if there is a cup in stock, false otherwise
   */
  public boolean isCupInStock() {
    return cupsNbr > 0;
  }

  /**
   * @return true if there is a spoon in stock, false otherwise
   */
  public boolean isSpoonInStock() {
    return spoonsNbr > 0;
  }

  /**
   * @param drink the Drink that may be in stock
   * @return true if there is a {@code drink} in stock, false otherwise
   */
  public boolean isDrinkInStock(Drink drink) {
    return drinkQty.get(drink) > 0;
  }

  /**
   * Removes {@code i} sugar cubes from the stock.
   * Throws an IllegalArgumentException if there is not {@code i} sugar cubes in stock.
   * 
   * @param i number of sugar cubes to remove.
   */
  public void removeSugarCubes(int i) {
    if (isSugarInStock(i)) {
      sugarCubesNbr -= i;
    } else {
      throw new IllegalArgumentException(
          "Can't remove " + i + " sugar cubes; only " + sugarCubesNbr + " remaining.");
    }
  }

  /**
   * Removes one cup from the stock.
   * Throws an IllegalArgumentException if there is no cup in stock.
   * If the number of cups becomes zero, notifies the specified IContext of the problem.
   * 
   * @param context the IContext to notify if there is no cups left
   */
  public void removeCup(IContext context) {
    if (isCupInStock()) {
      cupsNbr -= 1;
    } else {
      throw new IllegalArgumentException("Can't remove a cup when none in stock");
    }
    if (!isCupInStock()) {
      context.addProblem(NoCup.getInstance());
    }
  }

  /**
   * Removes one spoon from the stock.
   * Throws an IllegalArgumentException if there is no spoons in stock.
   */
  public void removeSpoon() {
    if (isSpoonInStock()) {
      spoonsNbr -= 1;
    } else {
      throw new IllegalArgumentException("Can't remove a spoon when none in stock");
    }
  }

  /**
   * Removes one {@code drink} from the stock.
   * Throws an IllegalArgumentException if this drink was not in stock.
   * 
   * @param drink the Drink to remove
   */
  public void removeDrink(Drink drink) {
    if (isDrinkInStock(drink)) {
      drinkQty.put(drink, drinkQty.get(drink) - 1);
    } else {
      throw new IllegalArgumentException("Can't remove a " + drink.getName() + "; none left in stock");
    }
  }

  /**
   * @return the number of sugar cubes in stock
   */
  public int getSugarCubesNbr() {
    return sugarCubesNbr;
  }

  /**
   * @return the number of cups in stock
   */
  public int getCupsNbr() {
    return cupsNbr;
  }

  /**
   * @return the number of spoons in stock
   */
  public int getSpoonsNbr() {
    return spoonsNbr;
  }

  /**
   * @param drink the drink whose stock value must be known
   * @return the number of the specified Drink in stock
   */
  public int getDrinkQty(Drink drink) {
    return drinkQty.get(drink);
  }

  /**
   * @return a List of the drinks the machine can dispense
   */
  public List<Drink> getDrinks() {
    return new ArrayList<Drink>(drinkQty.keySet());
  }

  /**
   * @return a String containing all the information about the current stock.
   */
  public String getInfo() {
    final StringBuilder sb = new StringBuilder(120);
    sb.append("Drink(s): \n");
    for (Drink drink: this.getDrinks()) {
      sb.append(drink.getName()).append(": ")
      .append(drinkQty.get(drink)).append(" available.\n");
    }

    sb.append('\n')
    .append(cupsNbr).append(" cup(s) available.\n")
    .append(sugarCubesNbr).append(" sugar cube(s) available.\n")
    .append(spoonsNbr).append(" spoon(s) available.\n");
    return sb.toString();
  }

  /**
   * Sets a new number of sugar cubes in stock. Logs the change that is done.
   * If you want to remove only some sugar cubes from the stock, you should use
   * {@code removeSugarCubes()} instead.
   * If the number is negative, throws an IllegalArgumentException.
   * 
   * @param newSugarCubesNbr the number of sugar cubes to set
   */
  void setSugarStock(int newSugarCubesNbr) {
    Utils.checkPositiveIntIllegal(newSugarCubesNbr, "cups stock");
    Utils.logChange(newSugarCubesNbr - this.sugarCubesNbr, newSugarCubesNbr, "sugar cube(s)");
    this.sugarCubesNbr = newSugarCubesNbr;
  }

  /**
   * Sets a new number of cups in stock. Logs the change that is done.
   * If you want to remove only one cup from the stock, you should use
   * {@code removeCup()} instead.
   * If the number of cups reaches 0 or is no more 0, updates the Context.
   * If the number is negative, throws an IllegalArgumentException.
   * 
   * @param newCupsNbr the number of cups to set
   * @param context the IContext to update if the number of cups reaches 0
   */
  void setCupStock(int newCupsNbr, IContext context) {
    Utils.checkPositiveIntIllegal(newCupsNbr, "cups stock");
    Utils.logChange(newCupsNbr - this.cupsNbr, newCupsNbr, "cup(s)");

    if (newCupsNbr == 0) {
      context.addProblem(NoCup.getInstance());
    } else if (this.cupsNbr == 0 && newCupsNbr > 0) {
      context.problemSolved(NoCup.getInstance());
    }
    this.cupsNbr = newCupsNbr;
  }

  /**
   * Sets a new number of spoons in stock. Logs the change that is done.
   * If you want to remove only one spoon from the stock, you should use
   * {@code removeSpoon()} instead.
   * If the number is negative, throws an IllegalArgumentException.
   * 
   * @param newSpoonsNbr the number of spoons to set
   */
  void setSpoonsStock(int newSpoonsNbr) {
    Utils.checkPositiveIntIllegal(newSpoonsNbr, "spoons stock");
    Utils.logChange(newSpoonsNbr - this.spoonsNbr, newSpoonsNbr, "spoon(s)");
    this.spoonsNbr = newSpoonsNbr;
  }

  /**
   * Updates the stock of {@code drink} to the {@code value} specified.
   * If you want to remove only one drink from the stock, you should use
   * {@code removeDrink(Drink)} instead.
   * Throws an IllegalArgumentException if {@code value} is negative.
   * Logs the change that is done.
   * 
   * @param drink the Drink whose stock must be changed
   * @param value the new value for the {@code drink} stock (must be positive)
   */
  void setDrinkStock(Drink drink, int value) {
    Utils.checkPositiveIntIllegal(value, drink.getName());
    Utils.logChange(value - drinkQty.get(drink), value, drink.getName() + "(s)");
    drinkQty.put(drink, value);
  }

}
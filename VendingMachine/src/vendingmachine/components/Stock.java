package vendingmachine.components;

import java.util.Map;

public class Stock {

  private int sugarCubesNbr;
  private int cupsNbr;
  private int spoonsNbr;
  private final Map<Drink, Integer> drinkQty;

  public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Map<Drink, Integer> drinkQty) {
    this.sugarCubesNbr = sugarCubesNbr;
    this.cupsNbr = cupsNbr;
    this.spoonsNbr = spoonsNbr;
    this.drinkQty = drinkQty;
  }

  public int getCupsNbr() {
    return cupsNbr;
  }

  public Map<Drink, Integer> getDrinkQty() {
    return drinkQty;
  }

  public int getSpoonsNbr() {
    return spoonsNbr;
  }

  public int getSugarCubesNbr() {
    return sugarCubesNbr;
  }

  public boolean isCupInStock() {
    return cupsNbr > 0;
  }

  public boolean isDrinkInStock(Drink drink) {
    return drinkQty.get(drink) > 0;
  }

  public boolean isSpoonInStock() {
    return spoonsNbr > 0;
  }

  public boolean isSugarInStock(int sugar) {
    return sugar <= sugarCubesNbr;
  }

  public void removeCup() {
    cupsNbr -= 1;
  }

  public void removeDrink(Drink drink) {
    drinkQty.put(drink, drinkQty.get(drink) - 1);
  }

  public void removeSpoon() { // Problem
    spoonsNbr -= 1;
  }

  public void removeSugarCubes(byte chosenSugar) {
    sugarCubesNbr -= chosenSugar;
  }

  public void setSugarCubesNbr(int sugarCubesNbr) {
    this.sugarCubesNbr = sugarCubesNbr;
  }
}
package vendingmachine.components;

import java.util.List;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.ui.IMachineGUI;
import vendingmachine.ui.TemperatureListener;

/**
 * Defines an interface that a vending machine selling hot drinks
 * should implement. 
 */
public interface IMachine {

  /**
   * Gives back change or cancel the current operation.
   */
  void cancel();

  /**
   * Confirms the current operation.
   */
  void confirm();
  
  /**
   * Called when "-" button is pressed.
   * Calls the {@code less()} method of the current State.
   */
  void less();
  
  /**
   * Increment a value (usually the sugar level).
   */
  void more();
  
  /**
   * Orders the drink specified or displays the price of the drink.
   * 
   * @param drink the Drink that is ordered
   */
  void drinkButton(Drink drink);
  
  /**
   * Simulates what happens when the client takes his change.
   */
  void takeChange();

  /**
   * Removes the cup from the cup container.
   */
  void takeCup();
  
  /**
   * Simulates the insertion of the specified Coin.
   * 
   * @param coin the Coin to insert
   */
  void coinInserted(Coin coin);
  
  /**
   * @return the List of all the Drink's the machine can dispense
   */
  List<Drink> getDrinks();
  
  /**
   * @return a String with all the information about the coins outside the machine
   */
  String getChangeOutInfo();

  /**
   * @return a String with all the current information about the machine
   */
  String getInfo();

  /**
   * @return the String message about the state that must be displayed by default
   */
  String getNorthText();

  /**
   * @return the String message about sugar that must be displayed by default
   */
  String getSugarText();
  
  void setChangeBool(boolean bool);

  void setCupBool(boolean cup, boolean spoon);

  <T extends IMachineGUI & TemperatureListener> void setObserver(T observer);
  
  void setWaterSupply(boolean bool);

  void setCoinStock(Coin coin, int value);

  void setDrinkStock(Drink drink, int value);
  
  void setCupStock(int value);
  
  void setSugarStock(int value);

  void setSpoonsStock(int value);

  /**
   * @return true if the stocks of the machine can be currently changed, false otherwise
   */
  boolean isAvailableForMaintenance();

  void setTemperature(int i);

  void repairStuckCoins();
  
  boolean isCoinAccepted(Coin coin);

}
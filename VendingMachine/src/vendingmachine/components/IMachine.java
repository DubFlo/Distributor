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
   * @param drink the Drink ordered
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

  /**
   * Sets the machineGUI attribute of the machine.
   * 
   * @param <T> must implement both IMachineGUI and TemperatureListener
   * @param observer the UI that must be updated by the machine and its heating system
   */
  <T extends IMachineGUI & TemperatureListener> void setUI(T observer);
  
  /**
   * @param bool true to enable the water supply, false to disable it
   */
  void setWaterSupply(boolean bool);

  /**
   * @param coin the Coin whose stock value must be changed
   * @param value the new stock value for the specified Coin
   */
  void setCoinStock(Coin coin, int value);

  /**
   * @param drink the Drink whose stock value must be changed
   * @param value the new stock value for the specified Drink
   */
  void setDrinkStock(Drink drink, int value);
  
  /**
   * @param value the new stock value for the cups
   */
  void setCupStock(int value);
  
  /**
   * @param value the new stock value for the sugar cubes
   */
  void setSugarStock(int value);

  /**
   * @param value the new stock value for the spoons
   */
  void setSpoonsStock(int value);

  /**
   * @return true if the stocks can be currently changed, false otherwise
   */
  boolean isAvailableForMaintenance();

  /**
   * @param temperature the new temperature to set
   */
  void setTemperature(int temperature);

  /**
   * Removes the StuckCoin Problem from the set of problems.
   */
  void repairStuckCoins();
  
  /**
   * @param coin the Coin that may be accepted
   * @return true if the Coin is accepted by the machine, false otherwise
   */
  boolean isCoinAccepted(Coin coin);

}
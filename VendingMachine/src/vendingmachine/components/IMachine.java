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
   * Decrement a value (usually the sugar level).
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
   * Removes all the coins that are waiting outside of the machine.
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
   * @return the List of all the Drinks the machine deals with
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

  void setCupBool(boolean bool);

  <T extends IMachineGUI & TemperatureListener> void setObserver(T observer);
  
  void setWaterSupply(boolean bool);

  void setCoinStock(Coin coin, int value);

  void setDrinkStock(Drink drink, int value);

  boolean isAvailableForMaintenance();

  void setTemperature(int i);

}
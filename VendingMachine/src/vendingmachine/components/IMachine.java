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

  void cancel();

  void coinInserted(Coin coin);

  void confirm();

  void drinkButton(Drink drink);
  
  String getChangeOutInfo();
  
  int getNbrDrinks();

  List<Drink> getDrinks();

  String getInfo();

  String getNorthText();

  void less();

  void more();

  void setChangeBool(boolean bool);

  void setCupBool(boolean bool);

  <T extends IMachineGUI & TemperatureListener> void setObserver(T observer);
  
  void setWaterSupply(boolean bool);

  void takeChange();

  void takeCup();

  String getSugarText();

  void setCoinStock(Coin coin, int value);

  void setDrinkStock(Drink drink, int value);

  boolean isAvailableForMaintenance();

  void setTemperature(int i);

}
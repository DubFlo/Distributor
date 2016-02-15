package vendingmachine.components;

import java.util.List;

import vendingmachine.states.State;
import vendingmachine.ui.ContextListener;
import vendingmachine.ui.TemperatureListener;

public interface EventListener {

  void cancel();

  void changeState(State state);

  void coinInserted(Coin coin);

  void confirm();

  void drinkButton(Drink drink);
  
  String getChangeOutInfo();
  
  int getNbrDrinks();

  List<Drink> getDrinks();

  String getInfo();

  String getNorthText();

  State getState();

  void less();

  void more();

  void setChangeBool(boolean b);

  void setCupBool(boolean b);

  <T extends ContextListener & TemperatureListener> void setObserver(T o);
  
  void setWaterSupply(boolean b);

  void takeChange();

  void takeCup();



}
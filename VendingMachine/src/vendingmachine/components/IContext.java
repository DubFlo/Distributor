package vendingmachine.components;

import java.util.List;
import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.states.State;

public interface IContext {// NOM DE MERDE

  List<Drink> getDrinks();

  void changeState(State instance);

  void addChangeOut(Map<Coin, Integer> moneyToGive); 

}

package vendingmachine.components;

import java.util.List;
import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.states.State;

/**
 * This interface is used by Stock and ChangeMachine objects to access the Context.
 */
public interface IContext {

  /**
   * @return the List of all the Drink's the machine can dispense
   */
  List<Drink> getDrinks();

  void changeState(State instance);

  void addChangeOut(Map<Coin, Integer> moneyToGive); 

}

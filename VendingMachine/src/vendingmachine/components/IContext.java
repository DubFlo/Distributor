package vendingmachine.components;

import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.states.Problem;
import vendingmachine.states.State;

/**
 * This interface is used by Stock and ChangeMachine objects to access the Context.
 */
public interface IContext {

  void addChangeOut(Map<Coin, Integer> moneyToGive);

  void problemSolved(Problem problem);

  void addProblem(Problem instance);

  State getState(); 

}

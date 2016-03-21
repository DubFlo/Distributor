package vendingmachine.components;

import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.states.Problem;
import vendingmachine.states.State;

/**
 * This interface is used by Stock and ChangeMachine objects to access the Context.
 */
public interface IContext {

  /**
   * Adds a Map of Coin's to the outside container.
   * 
   * @param moneyToGive the Map to add to {@code changeOut}
   */
  void addChangeOut(Map<Coin, Integer> moneyToGive);

  /**
   * Removes the specified Problem from the list of problems the machine is facing.
   * Changes the state if the machine was in that particular state.
   * 
   * @param problem the Problem that has been solved
   */
  void problemSolved(Problem problem);

  /**
   * Adds the specified problem to the list of problems the machine is facing.
   * The machine changes its state to this new Problem.
   * 
   * @param problem the Problem that happened
   */
  void addProblem(Problem problem);

  /**
   * @return the State the machine is currently in
   */
  State getState(); 

}

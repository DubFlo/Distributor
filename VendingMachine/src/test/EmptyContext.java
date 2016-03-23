package test;

import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.components.IContext;
import vendingmachine.states.Problem;
import vendingmachine.states.State;

public class EmptyContext implements IContext {

  public EmptyContext() {}

  @Override
  public void addChangeOut(Map<Coin, Integer> moneyToGive) {}

  @Override
  public void problemSolved(Problem problem) {}

  @Override
  public void addProblem(Problem instance) { }

  @Override
  public State getState() {
    return null;
  }

}

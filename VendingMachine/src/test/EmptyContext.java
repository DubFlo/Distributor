package test;

import java.util.List;
import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.IContext;
import vendingmachine.states.State;

public class EmptyContext implements IContext {

  public EmptyContext() { }

  @Override
  public List<Drink> getDrinks() {
    return null;
  }

  @Override
  public void changeState(State instance) {}

  @Override
  public void addChangeOut(Map<Coin, Integer> moneyToGive) {}

}

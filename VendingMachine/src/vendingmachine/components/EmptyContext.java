package vendingmachine.components;

import java.util.List;

import vendingmachine.Drink;
import vendingmachine.states.State;

public class EmptyContext implements IMachineInside {

  public EmptyContext() { }

  @Override
  public List<Drink> getDrinks() {
    return null;
  }

  @Override
  public void changeState(State instance) {}

}

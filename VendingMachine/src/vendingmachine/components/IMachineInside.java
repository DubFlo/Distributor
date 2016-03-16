package vendingmachine.components;

import java.util.List;

import vendingmachine.Drink;
import vendingmachine.states.State;

public interface IMachineInside {// NOM DE MERDE

  List<Drink> getDrinks();

  void changeState(State instance); 

}

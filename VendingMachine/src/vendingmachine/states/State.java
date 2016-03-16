package vendingmachine.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.Context;

/**
 * This abstract class defines default methods for the state of a Context object.
 * All the substates should implement the singleton design pattern.
 */
public abstract class State {

  protected static final Logger log = LogManager.getLogger("State");

  /**
   * Called when the button "Cancel" is pressed.
   * If not overridden, this method gives back change on what is currently inserted.
   */
  public void cancel(Context c) {
    if (c.getAmountInside() > 0) {
      if (c.getChangeMachine().isChangePossible(c.getAmountInside())) {
        c.giveChangeOnCancel();
      } else {
        c.setTemporaryNorthText("Unable to give back change");
      }
    }
  }

  /**
   * Simulates the insertion of the specified Coin.
   * Refuses the coin if not overridden.
   * 
   * @param coin the Coin to insert
   * @param c the Context associated with the State
   */
  public void coinInserted(Coin coin, Context c) {
    if (!isCoinStuck(c)) {
      c.setChangeBool(true);
      c.addChangeOutCoin(coin);
    }
  }
  
  /**
   * Called when the button "Confirm" is pressed. Does nothing if not overridden.
   */
  public void confirm(Context c) {}
  
  /**
   * Called when a drink button is pressed. Does nothing if not overridden.
   * 
   * @param drink the Drink associated with the button
   */
  public void drinkButton(Drink drink, Context c) {}
  
  /**
   * Called when the button "-" is pressed. Does nothing if not overridden.
   */
  public void less(Context c) {}
  
  /**
   * Called when the button "+" is pressed. Does nothing if not overridden.
   */
  public void more(Context c) {}

  public void entry(Context c) {}

  /**
   * Returns a message that should be displayed by the vending machine.
   * 
   * @param c the Context associated with the State
   * @return the String that should be displayed by the vending machine.
   */
  public abstract String getDefaultText(Context c);

  /**
   * Returns a String about the current information about the sugar.
   * Returns an empty String if not overridden.
   * 
   * @param c the Context associated with the State
   * @return a String containing information about sugar
   */
  public String getSugarText(Context c) {
    return "";
  }

  /**
   * Returns the name of the State class.
   */
  @Override
  public final String toString() {
    return this.getClass().getSimpleName(); // instead of getName() to avoid the package name
  }

  /**
   * @return true if the stocks can be changed without causing problems, false otherwise
   */
  public abstract boolean isAvailableForMaintenance();

  protected final boolean isCoinStuck(Context c) {
    if (Math.random() < c.COIN_STUCK_PROB) {
      c.changeState(StuckCoin.getInstance());
      return true;
    }
    return false;
  }
}
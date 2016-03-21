package vendingmachine.states;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.Context;

/**
 * This abstract class defines default methods for the state of a Context object.
 * All the substates should implement the singleton design pattern.
 */
public abstract class State {

  /**
   * Called when the button "Cancel" is pressed.
   * If not overridden, this method gives back change on what is currently inserted.
   * 
   * @param c the Context associated with the State
   */
  public void cancel(Context c) {
    if (c.getAmountInside() > 0) {
      if (c.isChangePossible(c.getAmountInside())) {
        c.giveChange(c.getAmountInside());
        c.updateUI();
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
    if (!c.isACoinStuck()) {
      if (!coinGetStuck(coin, c)) {
        c.setChangeBool(true);
        c.addChangeOutCoin(coin);
      }
    } else {
      c.addStuckCoin(coin);
      c.setTemporaryNorthText("Please do not add more coins!");
    }
  }

  /**
   * Called when the button "Confirm" is pressed. Does nothing if not overridden.
   * 
   * @param c the Context associated with the State
   */
  public void confirm(Context c) {}

  /**
   * Called when a drink button is pressed. Does nothing if not overridden.
   * 
   * @param drink the Drink associated with the button
   * @param c the Context associated with the State
   */
  public void drinkButton(Drink drink, Context c) {}

  /**
   * Called when the button "-" is pressed. Does nothing if not overridden.
   * 
   * @param c the Context associated with the State
   */
  public void less(Context c) {}

  /**
   * Called when the button "+" is pressed. Does nothing if not overridden.
   * 
   * @param c the Context associated with the State
   */
  public void more(Context c) {}

  /**
   * This method is called by the machine just after changing its state.
   * 
   * @param c the Context associated with the State
   */
  public void entry(Context c) {}

  /**
   * This method is called by the machine just before changing its state.
   * 
   * @param c the Context associated with the State
   */
  public void exit(Context c) {}

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
    return this.getClass().getSimpleName(); // instead of getName() to avoid package name
  }

  /**
   * Tells if the stocks may currently be changed without causing errors.
   * By default, returns false.
   * 
   * @return true if the stocks can be changed without problems, false otherwise
   */
  public boolean isAvailableForMaintenance() {
    return false;
  }

  /**
   * Checks if a the specified coin is stuck in the machine.
   * Uses the COIN_STUCK_PROB of the Context. Can not be overridden.
   * 
   * @param coin the Coin that may get stuck
   * @param c the Context associated with the State
   * @return true if the coins has been stuck, false otherwise
   */
  protected final boolean coinGetStuck(Coin coin, Context c) {
    if (Math.random() < c.COIN_STUCK_PROB) {
      c.addProblem(StuckCoin.getInstance());
      c.addStuckCoin(coin);
      return true;
    }
    return false;
  }

  /**
   * @return true if this state indicates a problem, false otherwise.
   */
  public boolean isProblem() {
    return false;
  }

}
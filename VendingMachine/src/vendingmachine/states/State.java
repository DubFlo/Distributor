package vendingmachine.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;

public abstract class State {

  protected static final Logger log = LogManager.getLogger("State");

  public void cancel(Context c) {
    if (c.getAmountInside() > 0 && c.getChangeMachine().isChangePossible(c.getAmountInside())) {
      // Cette condition est toujours vraie mais fait les calculs
      c.giveChangeOnCancel();
    }
  }

  public void coinInserted(Coin coin, Context c) {
    c.setChangeBool(true);
    c.addChangeOutCoin(coin);
  }
  
  /**
   * Called when the button "OK" is pressed. Does nothing by default.
   */
  public void confirm(Context c) {}
  
  /**
   * Called when a drink button is pressed. Does nothing by default.
   * @param drink the Drink associated with the button
   */
  public void drinkButton(Drink drink, Context c) {}
  
  /**
   * Called when the button "-" is pressed. Does nothing by default.
   */
  public void less(Context c) {}
  
  /**
   * Called when the button "+" is pressed. Does nothing by default.
   */
  public void more(Context c) {}

  public void entry(Context c) {}

  public abstract String getDefaultText(Context c);

  public String getSugarText(Context c) {
    return "";
  }

  @Override
  public final String toString() {
    return this.getClass().getSimpleName(); // instead of getName() to avoid the package name
  }

  public abstract boolean isAvailableForMaintenance();

}
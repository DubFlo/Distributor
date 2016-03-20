package vendingmachine.states;

import vendingmachine.components.Context;

/**
 * State reached when the user wants a sugared drink but there is no spoon in stock.
 * The user can choose whether or not he still wants the hot drink.
 */
public final class NoSpoon extends State {

  private static final NoSpoon INSTANCE = new NoSpoon();

  public static NoSpoon getInstance() {
    return INSTANCE;
  }

  private NoSpoon() {}

  /**
   * Gives back change and cancel the order.
   */
  @Override
  public void cancel(Context c) {
    super.cancel(c);
    c.changeState(Idle.getInstance());
  }

  /**
   * Confirms the order and changes the state to Asking.
   */
  @Override
  public void confirm(Context c) {
    c.changeState(Asking.getInstance());
  }
  
  @Override
  public String getDefaultText(Context c) {
    return "No spoon. Confirm to continue or Cancel";
  }

}

package vendingmachine.states;

import vendingmachine.components.Context;

/**
 * State reached from Idle when a drink is ordered and sugared.
 * Asks the user the sugar quantity he wants.
 */
public final class Asking extends State {

  private static final Asking INSTANCE = new Asking();
  
  /**
   * The maximal quantity of sugar in a drink.
   */
  private static final byte MAX_SUGAR = 5;

  public static Asking getInstance() {
    return INSTANCE;
  }

  private Asking() {}
  
  @Override
  public void entry(Context c) {
    c.setChosenSugar(0);
  }

  /**
   * Gives back change and changes the state of the machine to Idle.
   */
  @Override
  public void cancel(Context c) {
    super.cancel(c);
    c.changeState(Idle.getInstance());
  }

  /**
   * Gives the change and changes the state of the Context to preparing.
   * The change is possible and the drink is in stock (checked in Idle state)
   */
  @Override
  public void confirm(Context c) {
    c.changeState(Preparing.getInstance());
  }

  /**
   * Removes one from the sugar quantity wanted by the client.
   * Doesn't go obviously below zero.
   */
  @Override
  public void less(Context c) {
    if (c.getChosenSugar() > 0) {
      c.setChosenSugar(c.getChosenSugar() - 1);
    }
  }

  /**
   * Adds one to the sugar quantity wanted by the client.
   * Doesn't go above {@code MAX_SUGAR}.
   */
  @Override
  public void more(Context c) {
    if (c.getChosenSugar() < MAX_SUGAR && c.getStock().isSugarInStock(c.getChosenSugar() + 1)) {
      c.setChosenSugar(c.getChosenSugar() + 1);
    } else if (c.getChosenSugar() == MAX_SUGAR) {
      c.setTemporaryNorthText("Maximum quantity of sugar : " + MAX_SUGAR);
    } else {
      c.setTemporaryNorthText("No more sugar in stock");
    }
  }

  @Override
  public String getDefaultText(Context c) {
    return "Choose sugar quantity for " + c.getChosenDrink().getName();
  }

  @Override
  public String getSugarText(Context c) {
    return "Sugar: " + c.getChosenSugar() + "/" + MAX_SUGAR;
  }

}
package vendingmachine.states;

import vendingmachine.components.Context;

public final class Asking extends State {

  private static final Asking INSTANCE = new Asking();
  private static final byte MAX_SUGAR = 5;

  public static Asking getInstance() {
    return INSTANCE;
  }

  // Singleton design pattern
  private Asking() {}

  @Override
  public void cancel(Context c) {
    super.cancel(c);
    c.resetChosenSugar();
    c.changeState(Idle.getInstance());
  }

  @Override
  public void confirm(Context c) {
    if (c.getChosenSugar() > 0) {
      c.getStock().removeSugarCubes(c.getChosenSugar());
    }
    c.giveChangeOnDrink(); // On a vérifié que le change était possible dans Idle
    c.resetChosenSugar();
    c.changeState(Preparing.getInstance());
  }

  @Override
  public String getDefaultText(Context c) {
    return "Choose your sugar quantity with + and -";
  }

  @Override
  public String getSugarText(Context c) {
    return "Sugar: " + c.getChosenSugar() + "/" + MAX_SUGAR;
  }

  @Override
  public void less(Context c) {
    if (c.getChosenSugar() > 0 && c.getStock().isSugarInStock(c.getChosenSugar() + 1)) {
      c.decrementChosenSugar();
    }
  }

  @Override
  public void more(Context c) {
    if (c.getChosenSugar() < MAX_SUGAR && c.getStock().isSugarInStock(c.getChosenSugar() + 1)) {
      c.incrementChosenSugar();
    } else if (c.getChosenSugar() == MAX_SUGAR) {
      c.setTemporaryNorthText("Maximum quantity of sugar : " + MAX_SUGAR);
    } else {
      c.setTemporaryNorthText("No more sugar in stock");
    }
  }

  @Override
  public boolean isAvailableForMaintenance() {
    return false;
  }
  
}
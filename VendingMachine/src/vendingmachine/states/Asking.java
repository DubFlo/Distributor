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
    c.changeState(Idle.getInstance());
  }

  @Override
  public void confirm(Context c) {
    c.giveChange(c.getAmountInside() - c.getChosenDrink().getPrice());
    c.changeState(Preparing.getInstance());
  }

  @Override
  public String getDefaultText(Context c) {
    return "Choose sugar quantity for " + c.getChosenDrink().getName();
  }

  @Override
  public String getSugarText(Context c) {
    return "Sugar: " + c.getChosenSugar() + "/" + MAX_SUGAR;
  }

  @Override
  public void less(Context c) {
    if (c.getChosenSugar() > 0) {
      c.setChosenSugar(c.getChosenSugar() - 1);
    }
  }

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
  
}
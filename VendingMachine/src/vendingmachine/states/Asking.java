package vendingmachine.states;

import vendingmachine.components.Context;

public final class Asking extends State {

  private static final Asking INSTANCE = new Asking();
  private static final byte MAX_SUGAR = 5;
  
  private int chosenSugar = 0;

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
    if (chosenSugar > 0) {
      c.getStock().removeSugarCubes(chosenSugar);
    }
    c.giveChangeOnDrink(); // On a vérifié que le change était possible dans Idle
    c.changeState(Preparing.getInstance());
  }
  
  @Override
  public void exit(Context c) {
    chosenSugar = 0;
  }

  @Override
  public String getDefaultText(Context c) {
    return "Choose your sugar quantity with + and -";
  }

  @Override
  public String getSugarText(Context c) {
    return "Sugar: " + chosenSugar + "/" + MAX_SUGAR;
  }

  @Override
  public void less(Context c) {
    if (chosenSugar > 0) {
      chosenSugar -= 1;
    }
  }

  @Override
  public void more(Context c) {
    if (chosenSugar < MAX_SUGAR && c.getStock().isSugarInStock(chosenSugar + 1)) {
      chosenSugar += 1;
    } else if (chosenSugar == MAX_SUGAR) {
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
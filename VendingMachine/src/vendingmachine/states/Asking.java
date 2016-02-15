package vendingmachine.states;

import vendingmachine.components.Context;

public class Asking extends State {

  private static Asking instance;
  private static final byte MAX_SUGAR = 5;

  public static Asking instance() {
    if (instance == null) {
      instance = new Asking();
    }
    return instance;
  }

  // Singleton design pattern
  private Asking() {}

  @Override
  public void cancel(Context c) {
    super.cancel(c);
    c.resetChosenSugar();
    c.changeState(Idle.instance());
  }

  @Override
  public void confirm(Context c) {
    c.getStock().removeSugarCubes(c.getChosenSugar());
    c.giveChange(); // On a vérifié que le change était possible dans Idle()
    c.resetChosenSugar();
    c.changeState(Preparing.instance(c));
  }

  @Override
  public String getDefaultText(Context c) {
    return "Choose your sugar quantity with + and -";
  }

  @Override
  public String getSugarText(Context c) {
    return "Sugar: " + c.getChosenSugar() + "/" + MAX_SUGAR; // Affichage
    // plus joli
    // ?
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
}
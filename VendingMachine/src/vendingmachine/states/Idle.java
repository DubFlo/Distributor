package vendingmachine.states;

import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;

public class Idle extends State {

  private static Idle instance;

  public static Idle instance() {
    if (instance == null) {
      instance = new Idle();
    }
    return instance;
  }

  private Idle() {}

  @Override
  public void entry(Context c) {
    super.entry(c);
    if (c.getStock().getCupsNbr() == 0) {
      c.changeState(NoCup.instance());
    }
  }
  
  @Override
  public void coinInserted(Coin coin, Context c) {
    if (c.getChangeMachine().isCoinAccepted(coin)) {
      c.setAmountInside(c.getAmountInside() + coin.VALUE);
      c.insertCoin(coin);
      c.setTemporaryNorthText(Double.toString(coin.VALUE / 100.0) + " € inserted");
      log.info(coin.VALUE / 100.0 + " € inserted.");
    } else {
      super.coinInserted(coin, c);
      c.setTemporaryNorthText("Coin not recognized by the machine");
    }
    c.updateInfo(); // Le mettre ici ?????
  }

  @Override
  public void drinkButton(Drink d, Context c) {
    if (!c.getStock().isCupInStock()) {
      c.changeState(NoCup.instance());
    } else if (!c.getStock().isDrinkInStock(d)) {
      c.setTemporaryNorthText("Drink out of stock (otherwise " + d.getPrice() / 100.0 + " €)");
      log.warn("Attempt to order " + d.getName() + " but no left in stock.");
    } else if (c.isCupInside()) {
      c.setTemporaryNorthText("Please remove the drink before ordering");
    } else if (d.getPrice() > c.getAmountInside()) {
      c.setTemporaryNorthText("Price: " + d.getPrice() / 100.0 + " €");
    } else if (c.getChangeMachine().isChangePossible(c.getAmountInside() - d.getPrice())) {
      c.setChosenDrink(d);
      if (d.isSugar()) {
        c.changeState(Asking.instance());
      } else {
        c.giveChange();
        log.info(d.getName() + " ordered.");
        c.changeState(Preparing.instance(c));
      }
    } else {
      c.setTemporaryNorthText("Unable to give the exact change");
      log.warn(d.getName() + " ordered but machine unable to give the exact change.");
    }
  }

  @Override
  public String getDefaultText(Context c) {
    String msg = "Please make your choice";
    if (c.getAmountInside() > 0) {
      msg += " (" + c.getAmountInside() / 100.0 + " € entered)";
    }
    return msg;
  }
}
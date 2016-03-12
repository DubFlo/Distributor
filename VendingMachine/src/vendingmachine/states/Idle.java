package vendingmachine.states;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.Context;

public final class Idle extends State {

  private static final Idle INSTANCE = new Idle();

  public static Idle getInstance() {
    return INSTANCE;
  }

  private Idle() {}

  @Override
  public void entry(Context c) {
    super.entry(c);
    if (!c.getStock().isCupInStock()) {
      c.changeState(NoCup.getInstance());
    }
  }
  
  @Override
  public void coinInserted(Coin coin, Context c) {
    if (c.getChangeMachine().isCoinAccepted(coin)) {
      c.insertCoin(coin);
    } else {
      super.coinInserted(coin, c);
      c.setTemporaryNorthText("Coin not recognized by the machine");
    }
  }

  @Override
  public void drinkButton(Drink d, Context c) {
    if (!c.getStock().isCupInStock()) {
      c.changeState(NoCup.getInstance());
    } else if (!c.getStock().isDrinkInStock(d)) {
      c.setTemporaryNorthText("Drink out of stock (otherwise " + d.getPrice() / 100.0 + " €)");
      log.warn("Attempt to order " + d.getName() + " but no left in stock.");
    } else if (c.isCupInside()) {
      c.setTemporaryNorthText("Please remove the cup before ordering");
    } else if (d.getPrice() > c.getAmountInside()) {
      c.setTemporaryNorthText("Price: " + d.getPrice() / 100.0 + " €");
    } else if (c.getChangeMachine().isChangePossible(c.getAmountInside() - d.getPrice())) {
      c.setChosenDrink(d);
      if (d.isSugar()) {
        if (!c.getStock().isSpoonInStock()) {
          c.changeState(NoSpoon.getInstance());
        } else {
          c.changeState(Asking.getInstance());
        }
      } else {
        c.giveChange();
        log.info(d.getName() + " ordered.");
        c.changeState(Preparing.getInstance());
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

  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }
}
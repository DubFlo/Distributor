package vendingmachine.states;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.SoundLoader;
import vendingmachine.Utils;
import vendingmachine.components.Context;

public final class Idle extends State {

  private static final Idle INSTANCE = new Idle();

  public static Idle getInstance() {
    return INSTANCE;
  }

  private Idle() {}

  @Override
  public void coinInserted(Coin coin, Context c) {
    if (!coinGetStuck(coin, c)) {
      if (c.isCoinAccepted(coin)) {
        c.insertCoin(coin);
        SoundLoader.play(SoundLoader.getInstance().FOP);
      } else {
        c.setChangeBool(true);
        c.addChangeOutCoin(coin);
        c.setTemporaryNorthText("Coin not recognized by the machine");
      }
    }
  }

  @Override
  public void drinkButton(Drink d, Context c) {
    if (!c.getStock().isDrinkInStock(d)) {
      c.setTemporaryNorthText("Drink out of stock (otherwise " + d.getPrice() / 100.0 + " " + Utils.EURO + ")");
      log.warn("Attempt to order " + d.getName() + " but no left in stock.");
    } else if (c.isCupInside()) {
      c.setTemporaryNorthText("Please remove the cup before ordering");
    } else if (d.getPrice() > c.getAmountInside()) {
      c.setTemporaryNorthText("Price: " + d.getPrice() / 100.0 + " " + Utils.EURO);
    } else if (c.getChangeMachine().isChangePossible(c.getAmountInside() - d.getPrice())) {
      c.setChosenDrink(d);
      if (d.isSugar()) {
        if (!c.getStock().isSpoonInStock()) {
          c.changeState(NoSpoon.getInstance());
        } else {
          c.changeState(Asking.getInstance());
        }
      } else {
        c.changeState(Preparing.getInstance());
      }
    } else {
      c.setTemporaryNorthText("Unable to give the exact change");
      log.warn(d.getName() + " ordered but machine unable to give the exact change.");
    }
  }

  @Override
  public String getDefaultText(Context c) {
    String msg = "Please insert coins";

    if (c.getAmountInside() > 0) {
      msg = "Please make your choice" + " (" + c.getAmountInside() / 100.0 + " " + Utils.EURO + " entered)";
    }
    else if (c.areDrinksFree()) {
      msg = "Please make your choice, all is FREE";
    }
    return msg;
  }

  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }

}
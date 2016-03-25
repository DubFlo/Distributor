package vendingmachine.states;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.SoundLoader;
import vendingmachine.Utils;
import vendingmachine.components.Context;

/**
 * The default state of the vending machine.
 * Happens when no problem is happening and the user
 * is not in the process of ordering a drink.
 */
public final class Idle extends State {

  private static final Idle INSTANCE = new Idle();

  public static Idle getInstance() {
    return INSTANCE;
  }

  private Idle() {}

  /**
   * Simulates the insertion of a coin.
   * The coin may get stuck (based on the probability defined in the Context).
   * Then if the coin is accepted, it is added to the stock.
   * If not, it is immediately given back.
   */
  @Override
  public void coinInserted(Coin coin, Context c) {
    if (!coinGetStuck(coin, c)) {
      if (c.isCoinAccepted(coin)) {
        c.insertCoin(coin);
        SoundLoader.play(SoundLoader.getInstance().FOP);
      } else {
        c.addChangeOutCoin(coin);
        c.setTemporaryNorthText("Coin not recognized by the machine");
      }
    }
  }

  /**
   * Checks all the stocks necessary to order a drink.
   * There must be at least a the drink specified, a cup, enough money inserted, giving
   * back change must be possible. If one condition is not met, displays an error message.
   * If the drink is sugared, checks if there is a spoon and changes the specified
   * Context to the state NoSpoon or Asking. If not, the state becomes immediately Preparing.
   */
  @Override
  public void drinkButton(Drink d, Context c) {
    if (!c.getStock().isDrinkInStock(d)) {
      c.setTemporaryNorthText("Drink out of stock (otherwise "
          + d.getPrice() / 100.0 + " " + Utils.EURO + ")");
    } else if (c.isCupInside()) {
      c.setTemporaryNorthText("Please remove the cup before ordering");
    } else if (d.getPrice() > c.getAmountInside()) {
      c.setTemporaryNorthText("Price: " + d.getPrice() / 100.0 + " " + Utils.EURO);
    } else if (c.isChangePossible(c.getAmountInside() - d.getPrice())) {
      c.setChosenDrink(d);
      if (d.isSugar()) {
        if (c.getStock().isSpoonInStock()) {
          c.changeState(Asking.getInstance());
        } else {
          c.changeState(NoSpoon.getInstance());
        }
      } else {
        c.changeState(Preparing.getInstance());
      }
    } else {
      c.setTemporaryNorthText("Unable to give the exact change");
    }
  }

  @Override
  public String getDefaultText(Context c) {
    String msg = "Please insert coins";

    if (c.getAmountInside() > 0) {
      msg = "Please make your choice" + " ("
          + c.getAmountInside() / 100.0 + " " + Utils.EURO + " entered)";
    } else if (c.areDrinksFree()) {
      msg = "Please make your choice, everything is FREE";
    }
    return msg;
  }

  /**
   * Returns true because the stocks may be currently changed without causing errors.
   */
  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }

}
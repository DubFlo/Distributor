package vendingmachine.states;

import vendingmachine.Coin;
import vendingmachine.SoundLoader;
import vendingmachine.components.Context;

/**
 * State reached when everything is okay and a drink is ordered.
 * The user has to wait for the end of the preparation before inserting more coins.
 */
public final class Preparing extends State {

  private static final Preparing INSTANCE = new Preparing();

  public static Preparing getInstance() {
    return INSTANCE;
  }

  private Preparing() {}

  /**
   * Restarts the timer indicating the end of the preparation of a drink.
   * Plays the sound of a cup filling with water.
   */
  @Override
  public void entry(Context c) {
    super.entry(c);
    c.restartPreparingTimer();
    SoundLoader.play(SoundLoader.getInstance().FILLING);
  }

  /**
   * Refuses the coin and displays to wait for the end of the preparation.
   */
  @Override
  public void coinInserted(Coin coin, Context c) {
    super.coinInserted(coin, c);
    c.setTemporaryNorthText("Wait for the end of the preparation...");
  }

  /**
   * Warns the user it is too late to cancel the order.
   * Does not give back change.
   */
  @Override
  public void cancel(Context c) {
    c.setTemporaryNorthText("Too late to cancel the order!");
  }

  @Override
  public String getDefaultText(Context c) {
    return "Your drink is in preparation...";
  }

}
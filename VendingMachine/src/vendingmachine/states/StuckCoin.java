package vendingmachine.states;

import vendingmachine.Utils;
import vendingmachine.components.Context;

/**
 * State reached when a coin is stuck inside the machine.
 * Coins can be inserted but are only given back when the problem is repaired.
 */
public final class StuckCoin extends Problem {

  private static final StuckCoin INSTANCE = new StuckCoin();

  private StuckCoin() {}

  public static StuckCoin getInstance() {
    return INSTANCE;
  }

  /**
   * Enables the activation of the repair.
   */
  @Override
  public void entry(Context c) {
    c.enableRepair(true);
  }

  /**
   * Gives back change that has been stuck.
   * Disables the repair button.
   */
  @Override
  public void exit(Context c) {
    if (Utils.totalValue(c.getStuckCoins()) > 0) {
      c.addChangeOut(c.getStuckCoins());
      Utils.resetCoinsMap(c.getStuckCoins());
      c.setChangeBool(true);
    }
    c.enableRepair(false);
  }

  @Override
  public String getDefaultText(Context c) {
    return "A coin is stuck. Please call technician";
  }

}

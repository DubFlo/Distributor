package vendingmachine.states;

import java.util.Hashtable;
import java.util.Map;

import vendingmachine.Coin;
import vendingmachine.components.Context;

/**
 * State reached when a coin is stuck inside the machine.
 *  TO DOOOO
 */
public class StuckCoin extends State {

  private static final Map<Coin, Integer> coinsEnteredDespiteMessage = new Hashtable<Coin, Integer>();
  private static final StuckCoin INSTANCE = new StuckCoin();
  
  private StuckCoin() {}
  
  public static State getInstance() {
    for (Coin coin: Coin.COINS) {
      coinsEnteredDespiteMessage.put(coin, 0);
    }
    return INSTANCE;
  }

  @Override
  public void coinInserted(Coin coin, Context c) {
    coinsEnteredDespiteMessage.put(coin, coinsEnteredDespiteMessage.get(coin) + 1);
    c.setTemporaryNorthText("Please do not add more coins!");
  }
  
  @Override
  public String getDefaultText(Context c) {
    return "A coin is stuck. Please call technician";
  }

  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }

}

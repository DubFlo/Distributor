package vendingmachine.components;

import java.util.Map;

import vendingmachine.Coin;

/**
 *
 */
public class Change {

  private final Map<Coin, Integer> coinsStock;
  private final Map<Coin, Boolean> acceptedCoins;
  
  public Change(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
    this.coinsStock = coinsStock;
    this.acceptedCoins = acceptedCoins;
  }

  /**
   * @return
   */
  public Map<Coin, Integer> getCoinsStock() {
    return coinsStock;
  }

  /**
   * @return a Map<Coin, Boolean> that tells for each coin if it is accepted
   */
  public Map<Coin, Boolean> getAcceptedCoins() {
    return acceptedCoins;
  }

}
